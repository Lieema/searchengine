package com.epita.utils.logger;

import org.apache.logging.log4j.LogManager;

public abstract class Logger {
    protected static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(Logger.class);
}