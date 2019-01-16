namespace Tmon.Simplex.Interfaces
{
    /// <summary>
    /// 액션을 구독할 대상을 구현하는 인터페이스로 DisposableStore을 사용하는 경우
    /// Unsubscribe 호출시 해당 Disposable을 모두 Dispose 시킬 수 있습니다.
    /// </summary>
    public interface IActionSubscriber
    {
        /// <summary>
        /// 구독자의 고유 식별자
        /// </summary>
        string Id { get; }

        /// <summary>
        /// 액션의 구독을 정의를 등록할 메소드
        /// </summary>
        void Subscribe();

        /// <summary>
        /// Subscribe에서 등록한 액션의 구독을 모두 취소할 메소드
        /// </summary>
        void Unsubscribe();
    }
}
