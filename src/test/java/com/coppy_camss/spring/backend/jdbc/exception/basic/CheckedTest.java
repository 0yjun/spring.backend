package com.coppy_camss.spring.backend.jdbc.exception.basic;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@AllArgsConstructor
@Slf4j
public class CheckedTest {

    @Test
    void checked_catch(){
        Service service = new Service();
        service.callCatch();
    }
    @Test
    void checked_throw() throws MyCheckedException {
        Service service = new Service();
        Assertions.assertThatThrownBy(()->service.callThrow()).isInstanceOf(MyCheckedException.class);
    }


    static class MyCheckedException extends  Exception{
        public MyCheckedException(String message) {
            super(message);
        }
    }
    /**
     * 예외를 던지거나 잡거나 둘중 하나 실행
     */
    static  class Service{
        Repository repository = new Repository();
        /**
         * 예외를 잡아서 처리하는코드
         * */
        public void callCatch(){
            try {
                repository.call();
            } catch (MyCheckedException e) {
                log.info("exception is {}",e.getMessage(),e);
            }
        }
        /*
        * 체크 예외를 밖으로 던지는코드
        * 체크 예외는 예외를 잡지 않고 밖으로 던지려면 throws를 반드시 선언
        * */
        public void callThrow() throws Exception {
            repository.call();
        }
    }

    static  class Repository{
        public void call() throws MyCheckedException {
            throw new MyCheckedException("ex");
        }
    }
}
