package com.example.baselib.di.scope

import java.lang.annotation.Documented
import java.lang.annotation.Retention

import javax.inject.Scope

import java.lang.annotation.RetentionPolicy.RUNTIME

/**
 * A scoping annotation to permit objects whose lifetime should
 * conform to the life of the fragment to be memorized in the
 * correct component.
 */
@Scope
@Documented
@Retention(RUNTIME)
annotation class FragmentScope
