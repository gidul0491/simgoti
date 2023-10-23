package com.simg.simgoti.service;

import com.simg.simgoti.mapper.ClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HanaPremiumImpl implements HanaPremium {
    private final ClientMapper clientMapper;
    private int[] borderDay = {1,3,4,5,6,7,8,11,15,18,22,25,28,31,46,62,100};
    @Override
    public int selectByCovAgeGen(String covCode, int age, char gen, int day) throws Exception{
        int period = 0;

        for(int i = 0; i<borderDay.length; i++){
            if(day < borderDay[i]){
                period = borderDay[i-1];
                break;
            }
        }

        return clientMapper.selectByCovAgeGen(covCode,age,gen,period);
    }
}
