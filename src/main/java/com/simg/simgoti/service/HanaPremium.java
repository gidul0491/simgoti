package com.simg.simgoti.service;

import org.springframework.stereotype.Service;

@Service
public class HanaPremium {
    private int[] borderDay = {1,2,3,4,5,6,7,8,11,15,18,22,25,28,31,46,100};
    public int byCovAgeGen(String covCode, int age, char gen, int day) {
        int result = 0;
        int[] cov10120101;
        int[] cov10120102;
        int[] cov10120103;

        if(
//                age == 30 && gen == 'M'
                true
        ){
            cov10120101 = new int[] {2000,2000,2100,2950,3370,3790,4210,4640,5480,5900,6740,7170,8010,8430,10120,12650,16870};
            cov10120102 = new int[] {3630,3630,4540,6360,7270,8180,9090,10000,11820,12730,14550,15460,17280,18190,21830,27290};
            cov10120103 = new int[] {6430,6430,8030,11250,12860,14470,16070,17680,20900,22510,25720,27330,30550,32150,38590,48230,64310};

            if(covCode.equals("10120101")){
                for(int i=0; i<borderDay.length; i++){
                    if(day <= borderDay[i]){
                        result = cov10120101[i];
                        break;
                    }
                }
            }
            else if(covCode.equals("10120102")){
                for(int i=0; i<borderDay.length; i++){
                    if(day <= borderDay[i]){
                        result = cov10120102[i];
                        break;
                    }
                }
            }
            else if(covCode.equals("10120103")){
                for(int i=0; i<borderDay.length; i++){
                    if(day <= borderDay[i]){
                        result = cov10120103[i];
                        break;
                    }
                }
            }
        }



        return result;
    }
}
