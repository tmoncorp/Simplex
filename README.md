# Simplex

Simplex는 관심사를 분리하기 위해 고안된 패턴입니다.

Simplex에서는 실행 가능한 하나의 작업 단위를 Action이라고 부르며, Action은 입력값과 출력값을 선택적으로 가질 수 있습니다.
Action의 실행은 Store의 dispatch 메소드의 호출에의해 요청되며, 비동기로 실행됩니다.
완료된 작업 결과는 Store의 subscribe 메소드를 통해 구독이 요청된 구독자에게 멀티캐스트 됩니다.
