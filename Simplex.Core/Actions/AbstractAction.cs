
using System;
using System.Collections.Generic;
using System.Linq;
using Tmon.Simplex.Channels;
using Tmon.Simplex.Extensions;

namespace Tmon.Simplex.Actions
{
    public abstract class AbstractAction<TResult> : IAction<TResult>
    {
        internal IChannel Default { get; }

        public abstract IObservable<TResult> Process();

        public virtual bool Transform(Exception exception, out TResult result)
        {
            result = default;
            return false;
        }

        public IChannel Zip(params IChannel[] channels)
            => channels.Zip();
    }

    public abstract class AbstractAction<TParam, TResult>
        : AbstractAction<TResult>, IAction<TParam, TResult>
    {
        public abstract IObservable<TResult> Process(TParam param);
        
        public override IObservable<TResult> Process()
            => Process(default);
    }
}
