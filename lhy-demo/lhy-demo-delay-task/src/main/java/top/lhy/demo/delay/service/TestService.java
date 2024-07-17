package top.lhy.demo.delay.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TestService {

    public void testRun() {
        log.info("TestService testRun...");
    }

}
