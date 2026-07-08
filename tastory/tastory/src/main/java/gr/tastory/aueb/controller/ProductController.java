package gr.tastory.aueb.controller;

import gr.tastory.aueb.dto.ProductDto;
import gr.tastory.aueb.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import gr.tastory.aueb.model.Product;

import java.security.Principal;

@Controller
@RequestMapping("/restaurants/{restaurantId}/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String listProducts(@PathVariable Long restaurantId, Model model) {
        model.addAttribute("products", productService.findByRestaurant(restaurantId));
        model.addAttribute("restaurantId", restaurantId);
        return "products";
    }

    @GetMapping("/new")
    public String showProductForm(@PathVariable Long restaurantId, Model model) {
        model.addAttribute("productDto", new ProductDto());
        model.addAttribute("restaurantId", restaurantId);
        return "product-form";
    }

    @PostMapping
    public String addProduct(@PathVariable Long restaurantId,
                             @ModelAttribute ProductDto productDto,
                             Principal principal) {
        productService.addProduct(
                restaurantId,
                productDto.getName(),
                productDto.getPrice(),
                productDto.getDescription(),
                principal.getName()
        );
        return "redirect:/restaurants/" + restaurantId + "/products";
    }
    // 🗑️ DELETE — σβήνει το προϊόν
    @PostMapping("/{productId}/delete")
    public String deleteProduct(@PathVariable Long restaurantId,
                                @PathVariable Long productId,
                                Principal principal) {
        productService.deleteProduct(productId, principal.getName());
        return "redirect:/restaurants/" + restaurantId + "/products";
    }

    // ✏️ EDIT — δείχνει τη φόρμα με τα υπάρχοντα στοιχεία
    @GetMapping("/{productId}/edit")
    public String showEditForm(@PathVariable Long restaurantId,
                               @PathVariable Long productId,
                               Model model) {
        Product product = productService.findById(productId);

        ProductDto dto = new ProductDto();
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());

        model.addAttribute("productDto", dto);
        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("productId", productId);
        return "product-edit-form";
    }

    // ✏️ EDIT — αποθηκεύει τις αλλαγές
    @PostMapping("/{productId}/edit")
    public String updateProduct(@PathVariable Long restaurantId,
                                @PathVariable Long productId,
                                @ModelAttribute ProductDto productDto,
                                Principal principal) {
        productService.updateProduct(
                productId,
                productDto.getName(),
                productDto.getPrice(),
                productDto.getDescription(),
                principal.getName()
        );
        return "redirect:/restaurants/" + restaurantId + "/products";
    }

    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleSecurityException() {
        return "error-403";
    }
}