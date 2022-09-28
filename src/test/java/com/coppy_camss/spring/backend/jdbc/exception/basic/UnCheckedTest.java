package com.coppy_camss.spring.backend.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class UnCheckedTest {
    /**
     * runtimeexpetion을 상속받은 예외는 언체크 예외
     */
    @Test
    void UnCheckedTest(){
        Service service = new Service();
        service.Catch();
    }
    @Test
    void unCheckedThrow(){
        Service service = new Service();
        Assertions.assertThatThrownBy(()->service.callThrow()).isInstanceOf(MyUnCheckedException.class);
    }

    static class MyUnCheckedException extends RuntimeException{
        public MyUnCheckedException(String message){super(message);}
    }
    static class Repository{
        public void call(){
            throw new MyUnCheckedException("ex");
        }
    }

    /**
     * unckecked 예외는 예외를 잡거나 던지지 않아도 됨.
     * 예외를 잡지 않으면 자동으로 밖으로 던짐
     * */
    static class Service{
        Repository repository = new Repository();
        /*
        * 필요한 경우 예외를 잡아서 처리하면 됨
        * */
        public void Catch(){
            try {
                repository.call();
            }catch (MyUnCheckedException e){
                log.info("예외처리, {}",e.getMessage(),e);
            }
        }
        /*
        * 예외를 안잡아도 됨. 상위로 넘어감
        * throw 선언 필요 없음
        * */
        public void callThrow() {
            repository.call();
        }
    }

}
