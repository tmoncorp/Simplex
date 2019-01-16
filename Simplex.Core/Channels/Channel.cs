using System;
using System.Collections.Generic;

namespace Tmon.Simplex.Channels
{
    internal class Channel : IChannel
    {
        public string Id { get; } = Guid.NewGuid().ToString();
    }

    internal class ChannelZip : IChannel
    {
        public string Id { get; } = Guid.NewGuid().ToString();

        internal IEnumerable<IChannel> Channels { get; }

        internal ChannelZip(IEnumerable<IChannel> channels)
        {
            this.Channels = channels;
        }   
    }
}
