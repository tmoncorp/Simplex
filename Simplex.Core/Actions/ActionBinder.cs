using System;
using System.Collections.Generic;
using System.Text;

namespace Tmon.Simplex.Actions
{
    internal class ActionBinder<TAction, TResult> 
        : IActionBinder<TAction, TResult>
        where TAction : class, IAction<TResult>, new()
    {
        public TAction Action { get; }

        public ActionBinder()
        {
            Action = new TAction();
        }
    }

    internal class ActionBinder<TAction, TParam, TResult> 
        : ActionBinder<TAction, TResult>, IActionBinder<TAction, TParam, TResult>
       where TAction : class, IAction<TParam, TResult>, new()
    {
    }
}
