using System;
using System.Collections.Generic;
using System.Threading;

namespace Tmon.Simplex.Actions
{
    /// <summary>
    /// 실행할 하고 여러 채널을 통해 발행 및 결과값이 구독될 수 있는 Action의 정의
    /// </summary>
    /// <typeparam name="TResult">실행 결과의 데이터 타입</typeparam>
    public interface IAction<TResult> 
    {
        /// <summary>
        /// 에러가 발생하였을때, 에러를 정상 데이터로 변환하는 메소드
        /// </summary>
        /// <param name="exception">에러</param>
        /// <param name="result">정상 데이터로 변환된 결과</param>
        /// <returns>변환 성공 유무</returns>
        bool Transform(Exception exception, out TResult result);

        /// <summary>
        /// 실행할 비지니스 로직을 담고 있는 메소드
        /// </summary>
        /// <returns>반환할 결과 값</returns>
        IObservable<TResult> Process();
    }

    /// <summary>
    /// 실행할 하고 여러 채널을 통해 매개변수를 전달하여 발행하고, 그 결과값이 구독될 수 있는 Action의 정의
    /// </summary>
    /// <typeparam name="TParam">전달할 매개변수의 데이터 타입</typeparam>
    /// <typeparam name="TResult">실행 결과의 데이터 타입</typeparam>
    public interface IAction<TParam, TResult> 
        : IAction<TResult> 
    {
        /// <summary>
        /// 실행할 비지니스 로직을 담고 있는 메소드
        /// </summary>
        /// <param name="param">로직을 실행하기 위한 조건 데이터</param>
        /// <returns>반환할 결과 값</returns>
        IObservable<TResult> Process(TParam param);
    }
}
