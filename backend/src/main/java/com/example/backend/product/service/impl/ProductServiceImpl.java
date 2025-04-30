package com.example.backend.product.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.common.dto.PaginationResponse;
import com.example.backend.common.entity.AdditionalInfo;
import com.example.backend.common.entity.Brand;
import com.example.backend.common.entity.Category;
import com.example.backend.common.entity.Dimensions;
import com.example.backend.common.entity.Product;
import com.example.backend.common.entity.ProductCategory;
import com.example.backend.common.entity.ProductDetail;
import com.example.backend.common.entity.ProductImage;
import com.example.backend.common.entity.ProductOption;
import com.example.backend.common.entity.ProductOptionGroup;
import com.example.backend.common.entity.ProductPrice;
import com.example.backend.common.entity.ProductTag;
import com.example.backend.common.entity.Review;
import com.example.backend.common.entity.Seller;
import com.example.backend.common.entity.Tag;
import com.example.backend.common.repository.BrandRepository;
import com.example.backend.common.repository.CategoryRepository;
import com.example.backend.common.repository.ProductCategoryRepository;
import com.example.backend.common.repository.ProductDetailRepository;
import com.example.backend.common.repository.ProductImageRepository;
import com.example.backend.common.repository.ProductOptionGroupRepository;
import com.example.backend.common.repository.ProductOptionRepository;
import com.example.backend.common.repository.ProductPriceRepository;
import com.example.backend.common.repository.ProductRepository;
import com.example.backend.common.repository.ProductTagRepository;
import com.example.backend.common.repository.SellerRepository;
import com.example.backend.common.repository.TagRepository;
import com.example.backend.enums.ProductStatus;
import com.example.backend.product.dto.request.AdditionalInfoRequest;
import com.example.backend.product.dto.request.DimensionsRequest;
import com.example.backend.product.dto.request.ProductCreateRequest;
import com.example.backend.product.dto.request.ProductDetailRequest;
import com.example.backend.product.dto.request.ProductPriceRequest;
import com.example.backend.product.dto.request.ProductUpdateRequest;
import com.example.backend.product.dto.response.BrandResponse;
import com.example.backend.product.dto.response.ProductCategoryResponse;
import com.example.backend.product.dto.response.ProductDetailInfoResponse;
import com.example.backend.product.dto.response.ProductDetailResponse;
import com.example.backend.product.dto.response.ProductImageResponse;
import com.example.backend.product.dto.response.ProductListItemResponse;
import com.example.backend.product.dto.response.ProductListResponse;
import com.example.backend.product.dto.response.ProductOptionGroupResponse;
import com.example.backend.product.dto.response.ProductOptionResponse;
import com.example.backend.product.dto.response.ProductPriceResponse;
import com.example.backend.product.dto.response.ProductRatingResponse;
import com.example.backend.product.dto.response.SellerResponse;
import com.example.backend.product.dto.response.TagResponse;
import com.example.backend.product.service.ProductService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ProductPriceRepository productPriceRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductOptionGroupRepository productOptionGroupRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductTagRepository productTagRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final SellerRepository sellerRepository;
    private final TagRepository tagRepository;

    @Override
    public ProductDetailResponse createProduct(ProductCreateRequest request) {
        // Find related entities
        Seller seller = sellerRepository.findById(request.getSellerId())
                .orElseThrow(() -> new EntityNotFoundException("Seller not found"));
        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new EntityNotFoundException("Brand not found"));

        // Create product
        Product product = Product.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .shortDescription(request.getShortDescription())
                .fullDescription(request.getFullDescription())
                .seller(seller)
                .brand(brand)
                .status(ProductStatus.valueOf(request.getStatus()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Create product detail
        ProductDetail productDetail = createProductDetail(request.getDetail());
        productDetail.setProduct(product);
        product.setProductDetail(productDetail);

        // Create product price
        ProductPrice productPrice = createProductPrice(request.getPrice());
        productPrice.setProduct(product);
        product.setProductPrice(productPrice);

        // Create product images
        List<ProductImage> images = request.getImages().stream()
                .map(imageRequest -> {
                    ProductImage image = ProductImage.builder()
                            .url(imageRequest.getUrl())
                            .altText(imageRequest.getAltText())
                            .isPrimary(imageRequest.getIsPrimary())
                            .displayOrder(imageRequest.getDisplayOrder())
                            .build();
                    image.setProduct(product);
                    return image;
                })
                .collect(Collectors.toList());
        product.setImages(images);

        // Create product option groups and options
        List<ProductOptionGroup> optionGroups = request.getOptionGroups().stream()
                .map(groupRequest -> {
                    ProductOptionGroup group = ProductOptionGroup.builder()
                            .name(groupRequest.getName())
                            .displayOrder(groupRequest.getDisplayOrder())
                            .build();
                    group.setProduct(product);

                    List<ProductOption> options = groupRequest.getOptions().stream()
                            .map(optionRequest -> {
                                ProductOption option = ProductOption.builder()
                                        .name(optionRequest.getName())
                                        .additionalPrice(optionRequest.getAdditionalPrice())
                                        .sku(optionRequest.getSku())
                                        .stock(optionRequest.getStock())
                                        .displayOrder(optionRequest.getDisplayOrder())
                                        .build();
                                option.setOptionGroup(group);
                                return option;
                            })
                            .collect(Collectors.toList());
                    group.setOptions(options);
                    return group;
                })
                .collect(Collectors.toList());
        product.setOptionGroups(optionGroups);

        // Create product categories
        List<ProductCategory> productCategories = request.getCategories().stream()
                .map(categoryRequest -> {
                    Category category = categoryRepository.findById(categoryRequest.getCategoryId())
                            .orElseThrow(() -> new EntityNotFoundException("Category not found"));
                    ProductCategory productCategory = ProductCategory.builder()
                            .isPrimary(categoryRequest.getIsPrimary())
                            .build();
                    productCategory.setProduct(product);
                    productCategory.setCategory(category);
                    return productCategory;
                })
                .collect(Collectors.toList());
        product.setProductCategories(productCategories);

        // Create product tags
        List<ProductTag> productTags = request.getTags().stream()
                .map(tagId -> {
                    Tag tag = tagRepository.findById(tagId)
                            .orElseThrow(() -> new EntityNotFoundException("Tag not found"));
                    ProductTag productTag = new ProductTag();
                    productTag.setProduct(product);
                    productTag.setTag(tag);
                    return productTag;
                })
                .collect(Collectors.toList());
        product.setProductTags(productTags);

        // Save product and all related entities
        Product savedProduct = productRepository.save(product);

        return convertToProductDetailResponse(savedProduct);
    }

    @Override
    public ProductDetailResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        return convertToProductDetailResponse(product);
    }

    @Override
    public ProductListResponse getProducts(String search, Long categoryId, Long brandId, Long sellerId, String status, Pageable pageable) {
        Page<Product> products = productRepository
                .findByNameContainingAndProductCategoriesIdAndBrandIdAndSellerIdAndStatus(search, categoryId, brandId, sellerId, ProductStatus.valueOf(status), pageable);
        
        List<ProductListItemResponse> items = products.getContent().stream()
                .map(this::convertToProductListItemResponse)
                .collect(Collectors.toList());

        PaginationResponse pagination = new PaginationResponse(
                products.getNumber(),
                products.getSize(),
                products.getTotalElements(),
                products.getTotalPages()
        );

        ProductListResponse response = new ProductListResponse();
        response.setItems(items);
        response.setPagination(pagination);
        return response;
    }

    @Override
    public ProductDetailResponse updateProduct(Long id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        // Update basic product information
        product.setName(request.getName());
        product.setUpdatedAt(LocalDateTime.now());

        // Update seller if changed
        if (!product.getSeller().getId().equals(request.getSellerId())) {
            Seller seller = sellerRepository.findById(request.getSellerId())
                    .orElseThrow(() -> new EntityNotFoundException("Seller not found"));
            product.setSeller(seller);
        }

        // Update brand if changed
        if (!product.getBrand().getId().equals(request.getBrandId())) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new EntityNotFoundException("Brand not found"));
            product.setBrand(brand);
        }

        // Update category if changed
        if (!product.getProductCategories().stream()
                .anyMatch(pc -> pc.getCategory().getId().equals(request.getCategoryId()))) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            
            // Remove existing primary category
            product.getProductCategories().stream()
                    .filter(ProductCategory::getIsPrimary)
                    .findFirst()
                    .ifPresent(pc -> pc.setIsPrimary(false));
            
            // Add new primary category
            ProductCategory newPrimaryCategory = ProductCategory.builder()
                    .isPrimary(true)
                    .build();
            newPrimaryCategory.setProduct(product);
            newPrimaryCategory.setCategory(category);
            product.getProductCategories().add(newPrimaryCategory);
        }

        // Update options if provided
        if (request.getOptions() != null) {
            request.getOptions().forEach(optionUpdate -> {
                ProductOption option = productOptionRepository.findById(optionUpdate.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Product option not found"));
                option.setName(optionUpdate.getName());
                option.setStock(optionUpdate.getStock());
                option.setAdditionalPrice(optionUpdate.getPrice());
            });
        }

        Product updatedProduct = productRepository.save(product);
        return convertToProductDetailResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        productRepository.delete(product);
    }

    @Override
    public void updateProductStatus(Long id, String status) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        product.setStatus(ProductStatus.valueOf(status));
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    @Override
    public void updateProductStock(Long id, Long optionId, Integer quantity) {
        ProductOption option = productOptionRepository.findById(optionId)
                .orElseThrow(() -> new EntityNotFoundException("Product option not found"));
        
        if (!option.getOptionGroup().getProduct().getId().equals(id)) {
            throw new IllegalArgumentException("Option does not belong to the specified product");
        }
        
        option.setStock(quantity);
        productOptionRepository.save(option);
    }

    @Override
    public void updateProductPrice(Long id, Long optionId, Double price) {
        ProductOption option = productOptionRepository.findById(optionId)
                .orElseThrow(() -> new EntityNotFoundException("Product option not found"));
        
        if (!option.getOptionGroup().getProduct().getId().equals(id)) {
            throw new IllegalArgumentException("Option does not belong to the specified product");
        }
        
        option.setAdditionalPrice(price);
        productOptionRepository.save(option);
    }

    private ProductDetail createProductDetail(ProductDetailRequest request) {
        return ProductDetail.builder()
                .weight(request.getWeight())
                .dimensions(createDimensions(request.getDimensions()).toString())
                .materials(request.getMaterials())
                .countryOfOrigin(request.getCountryOfOrigin())
                .warrantyInfo(request.getWarrantyInfo())
                .careInstructions(request.getCareInstructions())
                .additionalInfo(createAdditionalInfo(request.getAdditionalInfo()).toString())
                .build();
    }

    private ProductPrice createProductPrice(ProductPriceRequest request) {
        return ProductPrice.builder()
                .basePrice(request.getBasePrice())
                .salePrice(request.getSalePrice())
                .costPrice(request.getCostPrice())
                .currency(request.getCurrency())
                .taxRate(request.getTaxRate())
                .build();
    }

    private Dimensions createDimensions(DimensionsRequest request) {
        return Dimensions.builder()
                .width(request.getWidth())
                .height(request.getHeight())
                .depth(request.getDepth())
                .build();
    }

    private AdditionalInfo createAdditionalInfo(AdditionalInfoRequest request) {
        return AdditionalInfo.builder()
                .assemblyRequired(request.getAssemblyRequired())
                .assemblyTime(request.getAssemblyTime())
                .build();
    }

    private ProductDetailResponse convertToProductDetailResponse(Product product) {
        ProductDetailResponse response = new ProductDetailResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setSlug(product.getSlug());
        response.setShortDescription(product.getShortDescription());
        response.setFullDescription(product.getFullDescription());
        response.setStatus(product.getStatus().name());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        // Set seller
        SellerResponse sellerResponse = new SellerResponse();
        sellerResponse.setId(product.getSeller().getId());
        sellerResponse.setName(product.getSeller().getName());
        response.setSeller(sellerResponse);

        // Set brand
        BrandResponse brandResponse = new BrandResponse();
        brandResponse.setId(product.getBrand().getId());
        brandResponse.setName(product.getBrand().getName());
        response.setBrand(brandResponse);

        // Set product detail
        ProductDetailInfoResponse detailResponse = new ProductDetailInfoResponse();
        detailResponse.setWeight(product.getProductDetail().getWeight());
        detailResponse.setMaterials(product.getProductDetail().getMaterials());
        detailResponse.setCountryOfOrigin(product.getProductDetail().getCountryOfOrigin());
        detailResponse.setWarrantyInfo(product.getProductDetail().getWarrantyInfo());
        detailResponse.setCareInstructions(product.getProductDetail().getCareInstructions());
        response.setDetail(detailResponse);

        // Set product price
        ProductPriceResponse priceResponse = new ProductPriceResponse();
        priceResponse.setBasePrice(product.getProductPrice().getBasePrice());
        priceResponse.setSalePrice(product.getProductPrice().getSalePrice());
        priceResponse.setCurrency(product.getProductPrice().getCurrency());
        priceResponse.setTaxRate(product.getProductPrice().getTaxRate());
        response.setPrice(priceResponse);

        // Set categories
        List<ProductCategoryResponse> categoryResponses = product.getProductCategories().stream()
                .map(pc -> {
                    ProductCategoryResponse categoryResponse = new ProductCategoryResponse();
                    categoryResponse.setId(pc.getCategory().getId());
                    categoryResponse.setName(pc.getCategory().getName());
                    categoryResponse.setSlug(pc.getCategory().getSlug());
                    categoryResponse.setIsPrimary(pc.getIsPrimary());
                    return categoryResponse;
                })
                .collect(Collectors.toList());
        response.setCategories(categoryResponses);

        // Set option groups
        List<ProductOptionGroupResponse> optionGroupResponses = product.getOptionGroups().stream()
                .map(group -> {
                    ProductOptionGroupResponse groupResponse = new ProductOptionGroupResponse();
                    groupResponse.setId(group.getId());
                    groupResponse.setName(group.getName());
                    groupResponse.setDisplayOrder(group.getDisplayOrder());

                    List<ProductOptionResponse> optionResponses = group.getOptions().stream()
                            .map(option -> {
                                ProductOptionResponse optionResponse = new ProductOptionResponse();
                                optionResponse.setId(option.getId());
                                optionResponse.setName(option.getName());
                                optionResponse.setAdditionalPrice(option.getAdditionalPrice());
                                optionResponse.setSku(option.getSku());
                                optionResponse.setStock(option.getStock());
                                optionResponse.setDisplayOrder(option.getDisplayOrder());
                                return optionResponse;
                            })
                            .collect(Collectors.toList());
                    groupResponse.setOptions(optionResponses);
                    return groupResponse;
                })
                .collect(Collectors.toList());
        response.setOptionGroups(optionGroupResponses);

        // Set images
        List<ProductImageResponse> imageResponses = product.getImages().stream()
                .map(image -> {
                    ProductImageResponse imageResponse = new ProductImageResponse();
                    imageResponse.setId(image.getId());
                    imageResponse.setUrl(image.getUrl());
                    imageResponse.setAltText(image.getAltText());
                    imageResponse.setIsPrimary(image.getIsPrimary());
                    imageResponse.setDisplayOrder(image.getDisplayOrder());
                    imageResponse.setOptionId(image.getOptionId());
                    return imageResponse;
                })
                .collect(Collectors.toList());
        response.setImages(imageResponses);

        // Set tags
        List<TagResponse> tagResponses = product.getProductTags().stream()
                .map(pt -> {
                    TagResponse tagResponse = new TagResponse();
                    tagResponse.setId(pt.getTag().getId());
                    tagResponse.setName(pt.getTag().getName());
                    return tagResponse;
                })
                .collect(Collectors.toList());
        response.setTags(tagResponses);

        // Set rating
        ProductRatingResponse ratingResponse = new ProductRatingResponse();
        ratingResponse.setAverage(calculateAverageRating(product.getReviews()));
        ratingResponse.setCount(product.getReviews().size());
        response.setRating(ratingResponse);

        return response;
    }

    private ProductListItemResponse convertToProductListItemResponse(Product product) {
        ProductListItemResponse response = new ProductListItemResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setSlug(product.getSlug());
        response.setShortDescription(product.getShortDescription());
        response.setBasePrice(product.getProductPrice().getBasePrice());
        response.setSalePrice(product.getProductPrice().getSalePrice());
        response.setCurrency(product.getProductPrice().getCurrency());
        response.setStatus(product.getStatus().name());
        response.setCreatedAt(product.getCreatedAt());

        // Set primary image
        product.getImages().stream()
                .filter(ProductImage::getIsPrimary)
                .findFirst()
                .ifPresent(image -> {
                    ProductImageResponse imageResponse = new ProductImageResponse();
                    imageResponse.setId(image.getId());
                    imageResponse.setUrl(image.getUrl());
                    imageResponse.setAltText(image.getAltText());
                    response.setPrimaryImage(imageResponse);
                });

        // Set brand
        BrandResponse brandResponse = new BrandResponse();
        brandResponse.setId(product.getBrand().getId());
        brandResponse.setName(product.getBrand().getName());
        response.setBrand(brandResponse);

        // Set seller
        SellerResponse sellerResponse = new SellerResponse();
        sellerResponse.setId(product.getSeller().getId());
        sellerResponse.setName(product.getSeller().getName());
        response.setSeller(sellerResponse);

        // Set rating
        response.setRating(calculateAverageRating(product.getReviews()));
        response.setReviewCount(product.getReviews().size());

        // Set in stock status
        boolean inStock = product.getOptionGroups().stream()
                .flatMap(group -> group.getOptions().stream())
                .anyMatch(option -> option.getStock() > 0);
        response.setInStock(inStock);

        return response;
    }

    private Double calculateAverageRating(List<Review> reviews) {
        if (reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }
} 