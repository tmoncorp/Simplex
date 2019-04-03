using System;
using System.Diagnostics;
using System.Reactive.Concurrency;
using System.Reactive.Linq;
using System.Reactive.Subjects;
using System.Runtime.CompilerServices;
using Tmon.Simplex.Actions;
using Tmon.Simplex.Exceptions;
using Tmon.Simplex.Store;

namespace Tmon.Simplex
{
    public static class Simplex
    {
        /// <summary>
        /// Store 인스턴스
        /// </summary>
        internal static IActionStore Store { get; private set; }

        /// <summary>
        /// 초기화 여부
        /// </summary>
        public static bool IsInitialized { get; private set; }

        /// <summary>
        /// Initialize() 호출 이전 상태로 초기화 합니다.  
        /// </summary>
        public static void Clear()
        {
            Store = null;
            Logger = null;
            MainThreadScheduler = null;
            ExceptionSubject = null;
            IsInitialized = false;
        }

        /// <summary>
        /// Simplex에서 사용할 환경값들을 설정합니다.
        /// 이 메소드는 반드시 ActionStore의 인스턴스를 조회하기 전에 실행되어야 합니다.
        /// 단 한번만 호출되어야 하며 여러번 호출시 예외가 발생됩니다.
        /// </summary>
        /// <param name="mainThreadScheduler">메인스레드 스케쥴러</param>
        /// <param name="exceptionHandler">글로벌 예외 처리기</param>
        /// <param name="exceptionThrottleMilliseconds">중복된 예외를 제거하기 위한 시간 버퍼로 기본값은 0.5초 입니다. 중복된 예외를 허용하려면 0을 설정하세요.</param>
        /// <param name="actionTimeoutMilliseconds">액션 처리가 지연될때 예외를 발생시킬 시간으로 기본값은 10초 입니다.</param>
        /// <param name="logger">로그기록 인터페이스</param>
        [MethodImpl(MethodImplOptions.Synchronized)]
        public static void Initialize(
            IScheduler mainThreadScheduler = null,
            Action<Exception> exceptionHandler = null,
            int exceptionThrottleMilliseconds = 500,
            int actionTimeoutMilliseconds = 10000,
            ILogger logger = null)
        {
            if (IsInitialized && Store != null)
            {
                logger.Write($"{Store.GetType().FullName} 타입용으로 이미 초기화 되어 있습니다.");
                return;
            }

            MainThreadScheduler = mainThreadScheduler;
            DefaultActionTimeout = TimeSpan.FromMilliseconds(actionTimeoutMilliseconds);
            Logger = logger;

            //등록된 메인스레드 스케쥴러가 존재하지 않는 경우 Rx기본 스케줄러 사용
            if (MainThreadScheduler == null)
                MainThreadScheduler = DefaultScheduler.Instance;

            //등록된 예외 처리기가 존재하지 않으면 예외를 발생시킴
            if (exceptionHandler == null)
            {
                exceptionHandler = (ex =>
                {
                    if (Debugger.IsAttached)
                        Debugger.Break();

                    MainThreadScheduler.Schedule(
                        action: () => throw new UnhandledErrorException(
                            @"IAction.Execute를 구현하는 객체가 오류를 발생시켰습니다. 
                              IAction<T>.Transform을 재정의하면 에러를 정상 데이터로 변환하여 구독할 수 있습니다.", ex)
                    );
                });
            }

            //예외를 배출할 주체 생성
            if (ExceptionSubject == null)
            {
                ExceptionSubject = new Subject<Exception>();
                ExceptionSubject.Buffer(TimeSpan.FromMilliseconds(exceptionThrottleMilliseconds))
                                .Where(x => x.Count > 0)
                                .SelectMany(x => x.ToObservable().Distinct(ex => ex.Message))
                                .Subscribe(x =>
                                    MainThreadScheduler.Schedule(() =>
                                        exceptionHandler.Invoke(x))
                                );
            }
            
            //초기화 완료
            IsInitialized = true;
        }
        
        /// <summary>
        /// 스토어 인스턴스를 조회합니다.
        /// 이 메소드 호출 전에 반드시 Simplex.Initialize() 메소드가 호출되어야만 합니다.
        /// </summary>
        /// <typeparam name="TActionSetGroup"></typeparam>
        /// <returns></returns>
        [MethodImpl(MethodImplOptions.Synchronized)]
        public static IActionStore<TActionBinderSet> GetStore<TActionBinderSet>()
            where TActionBinderSet : class, IActionBinderSet, new()
        {
            var typeName = typeof(Store<TActionBinderSet>).FullName;

            if (!IsInitialized)
                throw new TypeInitializationException(
                    fullTypeName: typeName,
                    innerException: new InvalidOperationException($"{nameof(Initialize)}메소드가 먼저 호출되어야 합니다."));

            if (Store == null)
                Store = new Store<TActionBinderSet>();

            if (Store.GetType() != typeof(Store<>).MakeGenericType(typeof(TActionBinderSet)))
                throw new InvalidCastException(
                    $"{Store.GetType()} 타입의 ActionStore를 {typeName}으로 형변환 할 수 없습니다.");
            
            return (Store<TActionBinderSet>)Store;
        }

        /// <summary>
        /// 액션 처리가 지연될때 예외를 발생시킬 시간으로 기본값은 10초 입니다.
        /// </summary>
        internal static TimeSpan DefaultActionTimeout { get; private set; }

        /// <summary>
        /// 예외를 배출시킬 주체
        /// </summary>
        internal static Subject<Exception> ExceptionSubject { get; private set; }

        /// <summary>
        /// 메인 쓰레드 스케줄러로 플랫폼별 구현되어야 합니다.
        /// </summary>
        public static IScheduler MainThreadScheduler { get; private set; }

        /// <summary>
        /// 로그기록 인터페이스
        /// </summary>
        internal static ILogger Logger { get; set; }
    }

    /// <summary>
    /// 단순 로그 기록용 인터페이스 
    /// (외부의 로거를 사용하기 위한 래퍼입니다.)
    /// </summary>
    public interface ILogger
    {
        Action<string> Write { get;}
    }

    public class SimpleLogger : ILogger
    {
        public Action<string> Write { get; }

        public SimpleLogger(Action<string> logger)
        {
               Write = logger;
        }
    }
}
