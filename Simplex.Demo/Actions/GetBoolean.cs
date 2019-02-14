using System;
using System.Collections.Generic;
using System.Reactive;
using System.Reactive.Linq;
using System.Text;
using Tmon.Simplex.Actions;
using Tmon.Simplex.Channels;

namespace SimplexDemo.Actions
{
    public class GetBoolean : AbstractAction<bool, bool>
    {
        public Channel ExCh1 { get; }

        public Channel ExCh2 { get; }

        public Channel ExCh3 { get; }

        public Channel ExCh4 { get; }

        public override IObservable<bool> Process(bool param)
        {
            return Observable.Return(param);
        }
    }

    public class GetIntegerString : AbstractAction<int, string>
    {
        public Channel ExCh1 { get; }

        public Channel ExCh2 { get; }

        public Channel ExCh3 { get; }

        public override IObservable<string> Process(int param)
        {
            return Observable.Return(param.ToString());
        }
    }

    public class SendSignal : AbstractAction<Unit>
    {
        public override IObservable<Unit> Process()
        {
            return Observable.Return(Unit.Default);
        }
    }
}
