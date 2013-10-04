package com.eaglesakura.gvolley.auth;

import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultString;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * 認証情報を保存しておくpref
 */
@SharedPref
public interface AuthPref {

    @DefaultString("")
    String token();

    @DefaultString("")
    String refreshToken();
}
