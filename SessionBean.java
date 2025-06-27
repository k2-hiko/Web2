package com.gips.nextapp;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * セッションスコープBean
 *
 * セッションスコープでやり取りするデータ
 *
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class SessionBean implements Serializable {

	String systemName = "模擬試験Webアプリ";
}
