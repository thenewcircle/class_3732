package com.hp.android.yamba;

import android.test.ActivityInstrumentationTestCase2;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

public class StatusActivityTest extends ActivityInstrumentationTestCase2<StatusActivity> {

    private static final String SHORT_TEXT = "A short post";
    private static final String LONG_TEXT = "Bacon ipsum dolor sit amet beef cow pork shankle. Pancetta tenderloin landjaeger ball tip corned beef chuck bacon flank. Jerky shoulder prosciutto flank shank, pork belly rump. Short loin bresaola ribeye, turkey strip steak frankfurter short ribs. Frankfurter filet mignon ground round pancetta ball tip meatloaf tongue porchetta shoulder short loin. Ham hock capicola sausage pork loin, meatloaf tongue doner porchetta ball tip chicken jowl t-bone meatball strip steak pork.";

    public StatusActivityTest() { super(StatusActivity.class); }

    private StatusActivity mActivity;

    private EditText mStatusText;
    private Button mStatusButton;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();

        mStatusText = (EditText) mActivity.findViewById(R.id.status_text);
        mStatusButton = (Button) mActivity.findViewById(R.id.status_button);
    }

    public void testPreconditions() {
        assertNotNull("Status text is null", mStatusText);
        assertNotNull("Status button is null", mStatusButton);
    }

    public void testStatusButton() {
        //Check for empty text
        assertTrue(TextUtils.isEmpty(mStatusText.getText()));

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatusText.setText(LONG_TEXT);
            }
        });
        getInstrumentation().waitForIdleSync();

        assertFalse("Status button should be disabled for long text", mStatusButton.isEnabled());

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatusText.setText(SHORT_TEXT);
            }
        });
        getInstrumentation().waitForIdleSync();

        assertTrue("Status button should be enabled for short text", mStatusButton.isEnabled());
    }
}
