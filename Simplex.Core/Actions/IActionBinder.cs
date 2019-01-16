using System;
using System.Collections.Generic;
using System.Reactive;
using System.Text;

namespace Tmon.Simplex.Actions
{
    /// <summary>
    /// 액션 래핑 인터페이스
    /// </summary>
    public interface IActionBinder { }

    /// <summary>
    /// 액션 래핑 인터페이스
    /// </summary>
    /// <typeparam name="TAction">액션 타입</typeparam>
    /// <typeparam name="TResult">액션의 결과값 반환 타입</typeparam>
    public interface IActionBinder<out TAction, TResult> : IActionBinder
        where TAction : IAction<TResult>
    {
        /// <summary>
        /// 액션
        /// </summary>
        TAction Action { get; }
    }

    /// <summary>
    /// 파라미터를 전달용 액션의 래핑 인터페이스
    /// </summary>
    /// <typeparam name="TAction"></typeparam>
    /// <typeparam name="TParam"></typeparam>
    /// <typeparam name="TResult"></typeparam>
    public interface IActionBinder<out TAction, TParam, TResult> : IActionBinder<TAction, TResult>
        where TAction : IAction<TParam, TResult>
    {
    }
}
