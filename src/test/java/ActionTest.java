import com.trileuco.gradle.DependencyTrackExtension;
import com.trileuco.gradle.UploadTask;
import org.gradle.api.Project;
import org.gradle.api.model.ObjectFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

/**
 * Description:
 * Author: meijinye
 * Date: 2023/7/9
 * Time: 12:15
 */
@RunWith(MockitoJUnitRunner.class)
public class ActionTest {
    @Mock
    private UploadTask uploadTask;
    @Mock
    private Project project;
    @Mock
    private ObjectFactory objectFactory;
    @Mock
    private DependencyTrackExtension dependencyTrackExtension;

    @Test
    public void upload() {
//        uploadTask.publish();
    }
}
