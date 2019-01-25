using System;
using System.Reactive;
using System.Reactive.Concurrency;
using System.Reactive.Linq;
using Tmon.Simplex;
using Tmon.Simplex.Actions;
using Tmon.Simplex.Channels;
using Tmon.Simplex.Store;
//using Tmon.Simplex.Extensions;

namespace SimplexDemo.Actions
{
    class Program
    {
        static void Main(string[] args)
        {
            Simplex.Initialize(logger: new SimpleLogger(text => System.Console.WriteLine(text)));
            using (var Store = Simplex.GetStore<MyActionSet>().ToDisposableStore("subscription-id"))
            {
                //Store.Subscribe(
                //    action: set => set.GetInteger,
                //    channel: act => act.Zip(act.ExCh1, act.ExCh2),
                //    observable: ob => ob.Distinct(),
                //    onNext: result => System.Console.WriteLine($"GetInteger Ch1 & Ch2 : {result}"));

                //Store.Subscribe(
                //    action: set => set.GetInteger,
                //    channel: act => act.ExCh2,
                //    onNext: result => System.Console.WriteLine($"GetInteger       Ch2 : {result}"));

                Store.Subscribe(
                    action: set => set.GetInteger,
                    //channel: act => act.ExCh1,
                    onNext: result => System.Console.WriteLine($"GetInteger Ch1       : {result}"));

                //Store.Subscribe(
                //    action: set => set.SendSignal,
                //    onNext: r => System.Console.WriteLine($"\n"));

                //Store.Dispatch(action: set => set.SendSignal);

                Store.Dispatch(
                    action: set => set.GetInteger,
                    channel: act => act.ExCh1,
                    parameter: 1);

                //Store.Dispatch(action: set => set.SendSignal);

               // for(int i= 0; i < 10000; i++)

                //Store.Dispatch(
                //    action: set => set.GetInteger,
                //    channel: act => act.Zip(act.ExCh2, act.ExCh1),
                //    parameter: 1);

                //Store.Dispatch(action: set => set.SendSignal);

                //Store.Dispatch(
                //    action: set => set.GetInteger,
                //    channel: act => act.ExCh2,
                //    parameter: 1);

                //Store.Dispatch(action: set => set.SendSignal);
            }
            GC.Collect();
            Console.ReadKey();
        }
    }
}
