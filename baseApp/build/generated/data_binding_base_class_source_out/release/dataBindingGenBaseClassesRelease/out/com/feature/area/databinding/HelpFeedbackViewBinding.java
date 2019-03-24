package com.feature.area.databinding;

import android.databinding.Bindable;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import com.motor.connect.feature.setting.help.HelpFeedbackViewModel;

public abstract class HelpFeedbackViewBinding extends ViewDataBinding {
  @NonNull
  public final ImageView btnActionLeft;

  @NonNull
  public final TextView txtTitle;

  @NonNull
  public final WebView webView;

  @Bindable
  protected HelpFeedbackViewModel mViewModel;

  protected HelpFeedbackViewBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, ImageView btnActionLeft, TextView txtTitle, WebView webView) {
    super(_bindingComponent, _root, _localFieldCount);
    this.btnActionLeft = btnActionLeft;
    this.txtTitle = txtTitle;
    this.webView = webView;
  }

  public abstract void setViewModel(@Nullable HelpFeedbackViewModel viewModel);

  @Nullable
  public HelpFeedbackViewModel getViewModel() {
    return mViewModel;
  }

  @NonNull
  public static HelpFeedbackViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static HelpFeedbackViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<HelpFeedbackViewBinding>inflate(inflater, com.feature.area.R.layout.help_feedback_view, root, attachToRoot, component);
  }

  @NonNull
  public static HelpFeedbackViewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static HelpFeedbackViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<HelpFeedbackViewBinding>inflate(inflater, com.feature.area.R.layout.help_feedback_view, null, false, component);
  }

  public static HelpFeedbackViewBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static HelpFeedbackViewBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (HelpFeedbackViewBinding)bind(component, view, com.feature.area.R.layout.help_feedback_view);
  }
}
