// Generated code from Butter Knife. Do not modify!
package pharamacy.eg.sala;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EditProfile_ViewBinding implements Unbinder {
  private EditProfile target;

  private View view7f090114;

  @UiThread
  public EditProfile_ViewBinding(EditProfile target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public EditProfile_ViewBinding(final EditProfile target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.profile_pic, "method 'onProfileImageClick'");
    view7f090114 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onProfileImageClick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    target = null;


    view7f090114.setOnClickListener(null);
    view7f090114 = null;
  }
}
