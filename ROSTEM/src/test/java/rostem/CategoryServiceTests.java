package rostem;

import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import rostem.model.dto.request.RequestCategory;
import rostem.model.dto.response.ResponseCategory;
import rostem.model.entities.Category;
import rostem.repository.materials.CategoryRepository;
import rostem.repository.users.RostemUserRepository;
import rostem.service.material.CategoryService;
import rostem.service.material.TutorialService;
import rostem.utils.mapper.CategoryMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceTests {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private RostemUserRepository rostemUserRepository;

    @Mock
    private TutorialService tutorialService;

    @Mock
    private CategoryMapper categoryMapper;

    private CategoryService categoryService;

    @Before
    public void setUp() {
        categoryService = new CategoryService(categoryRepository, rostemUserRepository, tutorialService);
    }

    @Test
    public void addCategory(){
        doReturn(of(createSimpleCategory())).when(categoryRepository).findById(eq(2L));
        doReturn(of(createSimpleCategory())).when(categoryRepository).findById(eq(1L));
        doReturn(createSimpleCategory()).when(categoryRepository).save(any());
        doReturn(createSimpleCategory()).when(categoryMapper).map((RequestCategory) any());
        doReturn(createSimpleCategory()).when(categoryMapper).map((Category) any());
        final ResponseCategory category = categoryService.createCategory(createSimpleCategory());
        assertThat(category, is(notNullValue()));
    }

    private RequestCategory createSimpleCategory(){
        RequestCategory category = new RequestCategory();
        category.setDescription("test_description");
        category.setName("test_name");
        return category;
    }
}
