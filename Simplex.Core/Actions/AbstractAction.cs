using System;
using System.Collections.Generic;
using System.Linq;
using Tmon.Simplex.Channels;

namespace Tmon.Simplex.Actions
{
    public abstract class AbstractAction<TResult> : IAction<TResult>
    {
        internal Channel Default { get; }

        public abstract IObservable<TResult> Process();

        public virtual bool Transform(Exception exception, out TResult result)
        {
            result = default;
            return false;
        }
    }

    public abstract class AbstractAction<TParam, TResult>
        : AbstractAction<TResult>, IAction<TParam, TResult>
    {
        public abstract IObservable<TResult> Process(TParam param);
        
        public override IObservable<TResult> Process()
            => Process(default);
    }
}
