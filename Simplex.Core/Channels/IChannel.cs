namespace Tmon.Simplex.Channels
{
    /// <summary>
    /// 작업단위(액션)을 발행하거나, 구독할 때 사용될 채널 
    /// </summary>
    public interface IChannel 
    {
        /// <summary>
        /// 채널의 식별자
        /// </summary>
        string Id { get; }
    }
}
