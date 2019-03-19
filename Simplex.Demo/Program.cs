using System;
using System.Linq;
using System.Reactive.Linq;
using Tmon.Simplex;
using Tmon.Simplex.Store;

namespace Tmon.Simplex.Demo.Actions
{
    class Program
    {
        static void Main(string[] args)
        {
            Simplex.Initialize(logger: new SimpleLogger(text => Console.WriteLine(text)));
            var Store = (Simplex.GetStore<MyActionSet>() as Store<MyActionSet>).ToDisposableStore("subscription-id");
            
            Store.Subscribe(
                action: set => set.GetInteger,
                channel: act => act.ExCh1 | act.ExCh2,
                observable: ob => ob.Distinct(),
                onNext: result => System.Console.WriteLine($"GetInteger Ch1 & Ch2 : {result}"));

            Store.Subscribe(
                action: set => set.GetInteger,
                channel: act => act.ExCh2,
                onNext: result => System.Console.WriteLine($"GetInteger       Ch2 : {result}"));

            Store.Subscribe(
                action: set => set.GetInteger,
                channel: act => act.ExCh3,
                onNext: result => System.Console.WriteLine($"GetInteger       Ch3 : {result}"));

            Store.Subscribe(
                action: set => set.GetInteger,
                channel: act => act.ExCh1,
                onNext: result => System.Console.WriteLine($"GetInteger Ch1       : {result}"));

            Store.Subscribe(
                action: set => set.SendSignal,
                onNext: r => System.Console.WriteLine($"\n"));

            Store.Dispatch(action: set => set.SendSignal);

            Store.Dispatch(
                action: set => set.GetInteger,
                channel: act => act.ExCh1,
                parameter: 1);

            Store.Dispatch(action: set => set.SendSignal);
                
            Store.Dispatch(
                action: set => set.GetInteger,
                channel: act => act.ExCh2 | act.ExCh1 | act.ExCh3,
                parameter: 1);

            Store.Dispatch(action: set => set.SendSignal);

            Store.Dispatch(
                action: set => set.GetInteger,
                channel: act => act.ExCh2,
                parameter: 1);

            Store.Subscribe(
                action: set => set.SecondsSet.GetRealtimeKeywordList,
                onNext: ranks =>
                {
                    Console.Clear();
                    foreach (var rank in ranks.OrderBy(r => r.Rank))
                    {
                        Console.WriteLine($"{rank.Keyword} {rank.State}");
                        
                    }
                });

            Store.Dispatch(action: set => set.SecondsSet.GetRealtimeKeywordList);
            
            
            Console.ReadKey();
            Store.Dispose();
        }
    }
}
