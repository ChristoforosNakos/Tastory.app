package gr.tastory.aueb.controller;

import gr.tastory.aueb.dto.ProductDto;
import gr.tastory.aueb.model.Product;
import gr.tastory.aueb.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
                             @Valid @ModelAttribute ProductDto productDto,
                             BindingResult bindingResult,
                             Model model,
                             Principal principal) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("restaurantId", restaurantId);
            return "product-form";
        }

        productService.addProduct(
                restaurantId,
                productDto.getName(),
                productDto.getPrice(),
                productDto.getDescription(),
                principal.getName()
        );
        return "redirect:/restaurants/" + restaurantId + "/products";
    }

    @PostMapping("/{productId}/delete")
    public String deleteProduct(@PathVariable Long restaurantId,
                                @PathVariable Long productId,
                                Principal principal) {
        productService.deleteProduct(productId, restaurantId, principal.getName());
        return "redirect:/restaurants/" + restaurantId + "/products";
    }

    @GetMapping("/{productId}/edit")
    public String showEditForm(@PathVariable Long restaurantId,
                               @PathVariable Long productId,
                               Model model,
                               Principal principal) {
        Product product = productService.findByIdForOwner(productId, restaurantId, principal.getName());

        ProductDto dto = new ProductDto();
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());

        model.addAttribute("productDto", dto);
        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("productId", productId);
        return "product-edit-form";
    }

    @PostMapping("/{productId}/edit")
    public String updateProduct(@PathVariable Long restaurantId,
                                @PathVariable Long productId,
                                @Valid @ModelAttribute ProductDto productDto,
                                BindingResult bindingResult,
                                Model model,
                                Principal principal) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("restaurantId", restaurantId);
            model.addAttribute("productId", productId);
            return "product-edit-form";
        }

        productService.updateProduct(
                productId,
                restaurantId,
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