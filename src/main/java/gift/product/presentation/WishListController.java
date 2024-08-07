package gift.product.presentation;

import gift.product.application.WishListService;
import gift.product.domain.AddWishListRequest;
import gift.product.domain.WishListResponse;
import gift.util.CommonResponse;
import gift.util.annotation.JwtAuthenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "WishListController", description = "위시리스트 관련 API")
@RestController
@RequestMapping("/api")
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }


    @Operation(summary = "위시리스트 조회", description = "사용자의 위시리스트를 조회합니다.")
    @GetMapping("/wishes")
    @JwtAuthenticated
    public ResponseEntity<?> getWishList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        String[] sortParams = sort.split(",");
        Sort sorting = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, sorting);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        List<WishListResponse> products = wishListService.getWishListByUserId(userId, pageable);
        return ResponseEntity.ok(products);
    }

    @JwtAuthenticated
    @Operation(summary = "위시리스트 생성", description = "위시리스트에 제품을 추가합니다.")
    @PostMapping("/wishes")
    public ResponseEntity<?> addProductToWishList(@RequestBody CreateWishListRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());
        wishListService.addProductToWishList(new AddWishListRequest(userId, request.getProductId()));
        return ResponseEntity.ok(new CommonResponse<>(null, "위시리스트에 제품 추가 성공", true));
    }

    @JwtAuthenticated
    @Operation(summary = "위시리스트에서 제품 삭제", description = "위시리스트에서 제품을 삭제합니다.")
    @DeleteMapping("/wishes/{productId}")
    public ResponseEntity<?> deleteProductFromWishList(
            @PathVariable Long productId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());
        wishListService.deleteProductFromWishList(userId, productId);
        return ResponseEntity.ok(new CommonResponse<>(null, "제품 삭제 성공", true));
    }
}
