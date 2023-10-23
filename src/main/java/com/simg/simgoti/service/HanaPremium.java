package com.simg.simgoti.service;

public interface HanaPremium {
    int selectByCovAgeGen(String covCode, int age, char gen, int day) throws Exception;
}
