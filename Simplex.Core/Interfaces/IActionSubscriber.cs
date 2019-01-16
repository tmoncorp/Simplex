namespace Tmon.Simplex.Interfaces
{
    public interface IActionSubscriber
    {
        string Id { get; }

        void Subscribe();

        void Unsubscribe();
    }
}
