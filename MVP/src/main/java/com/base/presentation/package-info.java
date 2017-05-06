/**
 * The <b>super</b> and <b>common</b> classes for the presentation layer in a project ... for any presentation project it
 * should divide it's structure into packages, each package indicates a <b>feature/screen</b>.
 * <br><br>
 * Any <b>feature/screen</b> consists of a {@link com.base.presentation.base.abstracts.features.AbstractActivity super Activity}
 * which implements the {@link com.base.presentation.base.abstracts.features.Feature Feature} interface.
 * <br><br>
 * <u>Dealing with an Activity :</u><br>
 * To make an Activity, you should extend a
 * {@link com.base.presentation.base.abstracts.features.AbstractActivity super Activity} that should have a
 * {@link com.base.presentation.base.abstracts.features.ViewBinder View Binder} that is responsible to
 * supply the core UI related implementation for it, and should have a
 * {@link com.base.presentation.models.Model Model} to have all the data required for this
 * <b>feature/screen</b> to do it's job.
 * <br>
 * For the {@link com.base.presentation.base.abstracts.features.ViewBinder View Binder}'s job is to locate
 * the {@link android.view.View Views} in the xml layout, and create a
 * {@link com.base.presentation.base.presentation.ViewModel View Model} instance,
 * {@link com.base.presentation.base.presentation.ViewModel#addView(android.view.View) add} to it
 * the related Views, then create a {@link com.base.presentation.base.presentation.Presenter Presenter}
 * that will be responsible to update the {@link com.base.presentation.base.presentation.ViewModel View Model}.
 * <br>
 * For the {@link com.base.presentation.models.Model Model}'s job, it should hold all the data required
 * by any class in the <b>feature/screen</b>, and it is responsible for communicating with a
 * {@link com.base.presentation.repos.base.Repository RepositoriesGroup} to get entities from
 * server or database or what ever source ... it is also responsible to maintain the state of the
 * <b>feature/screen</b> through holding the required data while navigating between it's attached views like
 * {@link com.base.presentation.base.abstracts.features.AbstractFragment Fragments}.
 * <br><br>
 * <u>Dealing with Fragments :</u><br>
 * To make a Fragment, you should extend a {@link com.base.presentation.base.abstracts.features.AbstractFragment super Fragment}
 * which has similar structure to the {@link com.base.presentation.base.abstracts.features.AbstractActivity super Activity},
 * which also needs a {@link com.base.presentation.base.abstracts.features.ViewBinder View Binder},
 * {@link com.base.presentation.base.presentation.ViewModel View Models},
 * {@link com.base.presentation.base.presentation.Presenter Presenters}.
 * <br>
 * How ever, the{@link com.base.presentation.base.abstracts.features.AbstractFragment super Fragment} uses the same
 * {@link com.base.presentation.models.Model Model} of the host
 * {@link com.base.presentation.base.abstracts.features.AbstractActivity super Activity}, so it does not need
 * a separate one.
 * <br>
 * To attach multiple {@link com.base.presentation.base.abstracts.features.AbstractFragment super Fragments} to
 * a {@link com.base.presentation.base.abstracts.features.AbstractActivity super Activity}, you can use
 * {@link com.base.presentation.base.abstracts.features.Feature#addFragment(int, android.support.v4.app.Fragment) addFragment()} and
 * {@link com.base.presentation.base.abstracts.features.Feature#removeFragment(android.support.v4.app.Fragment) removeFragment()}.
 * <br>
 * For multiple {@link com.base.presentation.base.abstracts.features.AbstractFragment Fragments}, it is better to
 * make a {@link com.base.presentation.base.presentation.Presenter Presenter} responsible for
 * navigation between the {@link com.base.presentation.base.abstracts.features.AbstractFragment Fragments}, and
 * adding and removing them.
 * <br><br>
 * <u>Navigation between Fragments in same host Activity :</u><br>
 * For navigation between {@link com.base.presentation.base.abstracts.features.AbstractFragment Fragments},
 * you can use something like :
 * <br>
 * {@link com.base.abstraction.state.FlowControllable}
 * <br>
 * To control the <b>next/back</b> navigation, and when pressing the back button on the device,
 * you can make the host {@link com.base.presentation.base.abstracts.features.AbstractActivity Activity} not finish
 * itself by throwing a {@link com.base.presentation.exceptions.OnBackPressStoppedException} ... this wont
 * cause a crash, it is handled by default in the
 * {@link com.base.presentation.base.abstracts.features.AbstractActivity super Activity}.
 * <p/>
 * <u>useful links :</u><br>
 * <a href="https://youtu.be/o_TH-Y78tt4">The Principles of Clean Architecture by Uncle Bob Martin</a><br>
 * <a href="https://youtu.be/R16OHcZJTno">GOTO 2015 • Going Reactive, An Android Architectural Journey • Matthias Käppler</a>
 */
package com.base.presentation;