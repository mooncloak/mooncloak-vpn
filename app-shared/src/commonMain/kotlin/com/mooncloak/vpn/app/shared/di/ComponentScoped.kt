package com.mooncloak.vpn.app.shared.di

import com.mooncloak.kodetools.konstruct.annotations.Scope
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.CONSTRUCTOR
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER

@Scope
@MustBeDocumented
@Target(CLASS, FUNCTION, CONSTRUCTOR, PROPERTY_GETTER)
public annotation class ComponentScoped
