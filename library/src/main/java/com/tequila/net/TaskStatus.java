package com.tequila.net;

public interface TaskStatus {
    int NONE = 0x520;
    int START = 0x521;
    int RUNNING = 0x522;
    int END = 0x523;
    int ERROR = 0x524;
    int SUCCESS = 0x525;
	int SERVER_ERROR = 0x526;
	int CACHE_HITTED = 0x527;
}