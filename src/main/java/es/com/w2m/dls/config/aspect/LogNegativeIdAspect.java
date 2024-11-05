package es.com.w2m.dls.config.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogNegativeIdAspect {
    // Define el punto de corte para métodos que solicitan una nave
    @Pointcut("execution(* es.com.w2m.dls.service.ShipServiceImpl.findById(..)) && args(id)")
    public void findById(Long id) {}

    // Define el consejo que se ejecuta después de que el método retorna
    @Before(value = "findById(id)", argNames = "id")
    public void logNegativeId(Long id) {
        if (id < 0) {
            log.error("Se solicitó una nave con un ID negativo: {}", id);
        }
    }
}
