using System;
using System.Collections.Generic;
using System.Reactive;
using System.Text;

namespace Tmon.Simplex.Actions
{
    public interface IActionBinder { }

    public interface IActionBinder<out TAction, TResult> : IActionBinder
        where TAction : IAction<TResult>
    {
        TAction Action { get; }
    }

    public interface IActionBinder<out TAction, TParam, TResult> : IActionBinder<TAction, TResult>
        where TAction : IAction<TParam, TResult>
    {
    }
}
