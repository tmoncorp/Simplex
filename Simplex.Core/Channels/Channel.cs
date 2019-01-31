using System;
using System.Collections.Generic;
using System.Linq;

namespace Tmon.Simplex.Channels
{
    public struct Channel
    {
        internal string Id { get { return Ids?[0]; } }

        internal string[] Ids { get; set; }
        
        public static Channel operator |(Channel ch1, Channel ch2)
        {
            return new Channel
            {
                Ids = ch1.Ids.Union(ch2.Ids).ToArray()
            };
        }
    }

}
