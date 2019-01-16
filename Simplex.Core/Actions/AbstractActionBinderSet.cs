using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;
using System.Reactive;
using System.Runtime.CompilerServices;
using System.Text;

namespace Tmon.Simplex.Actions
{
    /// <summary>
    /// 단위작업의 묶음 기능을 제공합니다.
    /// 단위작업 또는 묶을을 중첩하여 넣을 수 있습니다.
    /// Store의 Dispatch 또는 Subscribe에 "단위작업의 묶음"을 노출 시키기 위해서는
    /// 접근 가능한 프로퍼티를 구현해야 합니다. 
    /// 예) public ApiActionSet Api 
    ///        => GetOrAdd<ApiActionSet>();
    /// </summary>
    public abstract class AbstractActionBinderSet : IActionBinderSet
    {
        /// <summary>
        /// 단위작업 목록
        /// </summary>
        private readonly ConcurrentDictionary<string, IActionBinder> actionBinders;

        /// <summary>
        /// 단위작업의 묶음 목록
        /// </summary>
        private readonly List<IActionBinderSet> actionBinderSets;

        public AbstractActionBinderSet()
        {
            actionBinders = new ConcurrentDictionary<string, IActionBinder>();
            actionBinderSets = new List<IActionBinderSet>();
        }

        /// <summary>
        /// 단위작업 바인더를 조회합니다. 단위작업이 등록되어 있지 않으면 새롭게 등록하고 해당 객체를 리턴합니다.
        /// </summary>
        /// <typeparam name="TAction">단위작업의 타입</typeparam>
        /// <typeparam name="TResult">단위작업의 결과 반환타입</typeparam>
        /// <param name="key">단위작업의 등록 키</param>
        /// <returns>단위작업 바인더 인스턴스</returns>
        protected virtual IActionBinder<TAction, TResult> GetOrAdd<TAction, TResult>([CallerMemberName] string actionBinderKey = "")
            where TAction : class, IAction<TResult>, new()
        => GetOrAdd(actionBinderKey, k => new ActionBinder<TAction, TResult>()) as ActionBinder<TAction, TResult>;

        /// <summary>
        /// 단위작업 바인더를 조회합니다. 단위작업이 등록되어 있지 않으면 새롭게 등록하고 해당 객체를 리턴합니다.
        /// </summary>
        /// <typeparam name="TAction">단위작업의 타입</typeparam>
        /// <typeparam name="TParam">단위작업의 매개변수 타입</typeparam>
        /// <typeparam name="TResult">단위작업의 결과 반환 타입</typeparam>
        /// <param name="key">단위작업의 등록 키</param>
        /// <returns>단위작업 바인더 인스턴스</returns>
        protected virtual IActionBinder<TAction, TParam, TResult> GetOrAdd<TAction, TParam, TResult>([CallerMemberName] string actionBinderKey = "")
           where TAction : class, IAction<TParam, TResult>, new()
            => GetOrAdd(actionBinderKey, k => new ActionBinder<TAction, TParam, TResult>()) as ActionBinder<TAction, TParam, TResult>;

        private IActionBinder GetOrAdd(string actionBinderKey, Func<string, IActionBinder> valueFactory)
        {
            if (string.IsNullOrEmpty(actionBinderKey))
                throw new ArgumentNullException(nameof(actionBinderKey));

            var actionBinder = actionBinders.GetOrAdd(
                key: actionBinderKey,
                valueFactory: valueFactory);

            return actionBinder;
        }

        /// <summary>
        /// 단위작업의 묶음을 조회합니다. 단위작업 묶음이 등록되어 있지 않으면 새롭게 등록하고 해당 객체를 리턴합니다.
        /// </summary>
        /// <typeparam name="TActionBinderSet">단위작업의 묶음 타입</typeparam>
        /// <returns>단위작업 묶음 인스턴스</returns>
        public TActionBinderSet GetOrAdd<TActionBinderSet>()
            where TActionBinderSet : IActionBinderSet, new()
        {
            if (!(actionBinderSets.FirstOrDefault(x => x is TActionBinderSet) is TActionBinderSet actionSet))
            {
                actionSet = new TActionBinderSet();
                actionBinderSets.Add(actionSet);
            }

            return actionSet;
        }
    }
}
