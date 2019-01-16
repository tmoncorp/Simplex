using System;
using System.Collections.Generic;
using System.Text;
using Tmon.Simplex.Actions;

namespace Tmon.Simplex.Extensions
{
    public static class ActionExtension
    {
        internal static IObservable<TResult> InvokeProcess<TParam, TResult>(this IAction<TResult> source, TParam param)
        {
            Func<TParam, IObservable<TResult>> process;
            if (source is IAction<TParam, TResult> paramAction)
                process = (p => paramAction.Process(p));
            else
                process = (p => source.Process());

            return process.Invoke(param);
        }
    }
}
