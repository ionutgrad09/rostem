package rostem.service.material;

import static rostem.utils.ApiResponses.CATEGORY_ALREADY_EXISTS;
import static rostem.utils.ApiResponses.CATEGORY_NOT_FOUND;
import static rostem.utils.ApiResponses.CATEGORY_NOT_FOUND_FOR_USER;
import static rostem.utils.ApiResponses.USER_NOT_FOUND;
import static rostem.utils.ApiResponses.USER_NOT_FOUND_FOR_CATEGORY;
import static rostem.utils.mapper.CategoryMapper.map;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rostem.model.dto.request.RequestCategory;
import rostem.model.dto.response.ResponseCategory;
import rostem.model.entities.Category;
import rostem.model.users.RostemUser;
import rostem.repository.materials.CategoryRepository;
import rostem.repository.users.RostemUserRepository;
import rostem.utils.exception.RostemException;
import rostem.utils.mapper.CategoryMapper;

@Service
public class CategoryService {

    private final static Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;
    private final RostemUserRepository rostemUserRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, RostemUserRepository rostemUserRepository) {
        this.categoryRepository = categoryRepository;
        this.rostemUserRepository = rostemUserRepository;
    }

    public ResponseCategory getCategoryById(Long id) {
        logger.info("[CATEGORY] Searching for category: " + id);

        if (findCategoryById(id)) {
            return map(categoryRepository.findCategoryById(id));
        } else {
            throw new RostemException(CATEGORY_NOT_FOUND);
        }
    }

    public List<ResponseCategory> getAllCategories(String email) {
        logger.info("[CATEGORY] Get all categories");
        List<ResponseCategory> responseCategories = categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::map)
                .collect(Collectors.toList());

        markFavoriteCategories(email, responseCategories);
        return responseCategories;
    }

    public List<ResponseCategory> getAllCategoriesForAdmin() {
        logger.info("[CATEGORY] Get all categories");
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::map)
                .collect(Collectors.toList());
    }

    private void markFavoriteCategories(String email, List<ResponseCategory> responseCategories) {
        List<Category> favoriteCategories = rostemUserRepository.findByEmail(email).getFavoriteCategories();

        for (ResponseCategory responseCategory : responseCategories) {
            for (Category chapter : favoriteCategories) {
                if (responseCategory.getId().equals(chapter.getId())) {
                    responseCategory.setMarkedAsFavorite(true);
                }
            }
        }
    }

    public ResponseCategory createCategory(RequestCategory requestCategory) {
        final String name = requestCategory.getName();
        logger.info("[CATEGORY] Trying to add a new category " + name);

        if (findCategoryByName(name)) {
            throw new RostemException(CATEGORY_ALREADY_EXISTS);
        } else {
            Category category = map(requestCategory);
            return map(categoryRepository.save(category));
        }
    }

    @Transactional
    public void deleteCategories(List<Long> ids) {

        for (Long id : ids) {
            logger.info("[CATEGORY] Trying to delete category " + id);
            if (!findCategoryById(id)) {
                throw new RostemException(CATEGORY_NOT_FOUND);
            } else {
                categoryRepository.deleteById(id);
            }
        }
    }

    @Transactional
    public ResponseCategory updateCategory(Long id, RequestCategory requestCategory) {
        logger.info("[CATEGORY] Trying to update category " + id);

        if (!findCategoryById(id)) {
            throw new RostemException(CATEGORY_NOT_FOUND);
        } else {
            Category category = map(requestCategory);
            category.setId(id);
            return map(categoryRepository.save(category));
        }
    }

    public void addFavoriteCategory(String email, Long id) {
        logger.info("[USER_CATEGORY] Trying to add set a category as favorite for user " + email);

        if (!findUserByEmail(email)) {
            throw new RostemException(USER_NOT_FOUND);
        } else if (!findCategoryById(id)) {
            throw new RostemException(CATEGORY_NOT_FOUND);
        } else {
            Category category = categoryRepository.findCategoryById(id);
            RostemUser rostemUser = rostemUserRepository.findByEmail(email);

            category.getUsers().add(rostemUser);
            rostemUser.getFavoriteCategories().add(category);
            categoryRepository.save(category);
            rostemUserRepository.save(rostemUser);
        }
    }

    public List<ResponseCategory> getFavoritesCategories(String email) {
        logger.info("[USER_CATEGORY] Trying to get favorites categories for user " + email);

        if (!findUserByEmail(email)) {
            throw new RostemException(USER_NOT_FOUND);
        } else {
            return rostemUserRepository.findByEmail(email).getFavoriteCategories()
                    .stream()
                    .map(CategoryMapper::map)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public void deleteFavoriteCategory(String email, Long id) {
        logger.info("[USER_CATEGORY] Trying to delete a favorite category from user's " + email + " list");

        if (!findUserByEmail(email)) {
            throw new RostemException(USER_NOT_FOUND);
        } else if (!findCategoryById(id)) {
            throw new RostemException(CATEGORY_NOT_FOUND);
        } else {
            Category category = categoryRepository.findCategoryById(id);
            RostemUser rostemUser = rostemUserRepository.findByEmail(email);
            deleteCategoryFromUser(category, rostemUser);
            deleteUserFromCategory(category, rostemUser);
        }
    }

    private void deleteCategoryFromUser(Category category, RostemUser rostemUser) {
        List<Category> userCategories = rostemUser.getFavoriteCategories();
        if (userCategories.stream().anyMatch(x -> x.getId().equals(category.getId()))) {
            userCategories.remove(category);
            rostemUser.setFavoriteCategories(userCategories);
            rostemUserRepository.save(rostemUser);
        } else {
            throw new RostemException(CATEGORY_NOT_FOUND_FOR_USER);
        }
    }

    private void deleteUserFromCategory(Category category, RostemUser rostemUser) {
        List<RostemUser> categoriesUser = category.getUsers();
        if (categoriesUser.stream().anyMatch(x -> x.getEmail().equals(rostemUser.getEmail()))) {
            categoriesUser.remove(rostemUser);
            category.setUsers(categoriesUser);
            categoryRepository.save(category);
        } else {
            throw new RostemException(USER_NOT_FOUND_FOR_CATEGORY);
        }
    }


    private boolean findUserByEmail(String email) {
        return rostemUserRepository.findByEmail(email) != null;
    }

    private boolean findCategoryById(Long id) {
        return categoryRepository.findCategoryById(id) != null;
    }

    private boolean findCategoryByName(String name) {
        return categoryRepository.findCategoryByName(name) != null;
    }
}
