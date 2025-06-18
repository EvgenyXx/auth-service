package com.example.auth.service.redis;


import com.example.auth.exception.user.AccountBlockedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LoginAttemptService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final int MAX_ATTEMPTS = 5;
    private static final long BLOCK_TIME_MINUTES = 15;
    private static final String PREFIX = "login_attempts:";


    public void loginFailed (String numberPhone){
        String key = PREFIX + numberPhone;
        int attempts = getAttempts(numberPhone) + 1;
        redisTemplate.opsForValue().set(key,attempts,BLOCK_TIME_MINUTES, TimeUnit.MINUTES);
    }

    public void loginSuccess(String numberPhone){
        redisTemplate.delete(PREFIX + numberPhone);
    }

    public long getBlockTimeRemaining(String numberPhone) {
        Long expire = redisTemplate.getExpire(PREFIX + numberPhone, TimeUnit.MINUTES);
        return expire != null ? expire : 0;
    }




    public boolean isBlock(String numberPhone){
        return getAttempts(numberPhone)>=MAX_ATTEMPTS;
    }

    public void checkIfBlocked(String numberPhone){
        if (isBlock(numberPhone)){
            long remainingTime = getBlockTimeRemaining(numberPhone);
            throw new AccountBlockedException(
                    "Аккаунт заблокирован! Попробуйте через " + remainingTime + " минут."
            );
        }
    }



    private int getAttempts(String numberPhone){
        String key = PREFIX + numberPhone;
        Integer attempts = (Integer) redisTemplate.opsForValue().get(key);
        return attempts != null ? attempts : 0;
    }

    public AttemptStatus getAttemptsStatus(String numberPhone){
        int attempts = getAttempts(numberPhone);
        boolean isBlocked = attempts >= MAX_ATTEMPTS;

        return new AttemptStatus(
                isBlocked,
                attempts,
                isBlocked ? getBlockTimeRemaining(numberPhone) : 0,
                MAX_ATTEMPTS - attempts
        );
    }



    public record AttemptStatus(
            boolean isBlocked,
            int attemptsCount,
            long blockTimeRemaining,
            int remainingAttempts
    ) {}


}
