# Simplex

Simplex는 관심사를 분리하기 위해 고안된 패턴입니다.
MVVM등의 다른 패턴과 함께 사용 될 수 있습니다.

Simplex에서는 실행 가능한 하나의 작업 단위를 Action이라고 부르며, Action은 입력값과 출력값을 선택적으로 가질 수 있습니다.
Action의 실행은 Store의 dispatch 메소드의 호출에의해 요청되며, 비동기로 실행됩니다.
완료된 작업 결과는 Store의 subscribe 메소드를 통해 구독이 요청된 구독자에게 멀티캐스트 됩니다.

현재 C# (NetStandard 2.0), Java 8, Kotlin 버전으로 개발이 되어 다양한 환경에서 사용 할 수 있으며,
특히 Xamarin.Forms 환경에서 MVVM 패턴과 사용시 기존 대비 눈에 띄게 정돈된 코드를 경험 할 수 있게 됩니다.
