using System;
using System.Collections.Generic;
using System.Linq;
using System.Reactive.Concurrency;
using System.Text;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Tmon.Simplex.Concurrency;
using Tmon.Simplex.Interfaces;

namespace Tmon.Simplex
{
    public class Platform : IPlatform
    {
        private Platform() { }

        public static  IPlatform Current { get; } = new Platform();

        public IScheduler UIThreadScheduler { get; }
            = new UIThreadScheduler(new Handler(Looper.MainLooper), Looper.MainLooper.Thread.Id);
    }
}