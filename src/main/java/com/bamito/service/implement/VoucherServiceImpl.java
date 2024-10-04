package com.bamito.service.implement;

import com.bamito.dto.request.product.UpdateVoucherRequest;
import com.bamito.dto.request.product.VoucherRequest;
import com.bamito.dto.response.PaginationResponse;
import com.bamito.dto.response.product.VoucherResponse;
import com.bamito.entity.Voucher;
import com.bamito.exception.CustomizedException;
import com.bamito.exception.ErrorCode;
import com.bamito.repository.IVoucherRepository;
import com.bamito.service.IVoucherService;
import com.cloudinary.Cloudinary;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bamito.util.CommonFunction.createSortOrder;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherServiceImpl implements IVoucherService {
    IVoucherRepository voucherRepository;
    Cloudinary cloudinary;

    @Override
    public void createVoucher(VoucherRequest request) throws IOException {
        if (voucherRepository.existsById(request.getId()))
            throw new CustomizedException(ErrorCode.VOUCHER_EXISTED);

        Map cloudinaryObject = cloudinary.uploader()
                .upload(request.getImage().getBytes(), Map.of("folder", "bamito"));
        var imageUrl = cloudinaryObject.get("secure_url");
        var imageId = cloudinaryObject.get("public_id");

        Voucher voucher = Voucher.builder()
                .id(request.getId())
                .discount(request.getDiscount())
                .quantity(request.getQuantity())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .imageId(imageId.toString())
                .imageURL(imageUrl.toString())
                .build();

        voucherRepository.save(voucher);
    }

    @Override
    public PaginationResponse<VoucherResponse> getAllVouchers(int page, int size, String filter, List<String> sort, boolean pagination) {
        if (!pagination) {
            List<Voucher> voucherList = voucherRepository.findAll();
            List<VoucherResponse> voucherResponseList = voucherList.stream()
                    .map(v -> VoucherResponse.builder()
                            .id(v.getId())
                            .discount(v.getDiscount())
                            .quantity(v.getQuantity())
                            .startTime(v.getStartTime())
                            .endTime(v.getEndTime())
                            .imageURL(v.getImageURL())
                            .build()).toList();
            return PaginationResponse.<VoucherResponse>builder()
                    .content(voucherResponseList)
                    .build();
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sort)));
        Page<Voucher> voucherPage;
        try {
            if (!filter.isBlank()) {
                voucherPage = voucherRepository.findAllByIdContaining(filter, pageable);
            } else {
                voucherPage = voucherRepository.findAll(pageable);
            }
        } catch (RuntimeException e) {
            throw new CustomizedException(ErrorCode.INVALID_SORT_PARAMETER);
        }
        List<Voucher> voucherList = voucherPage.getContent();
        List<VoucherResponse> voucherResponseList = voucherList.stream()
                .map(v -> VoucherResponse.builder()
                        .id(v.getId())
                        .discount(v.getDiscount())
                        .quantity(v.getQuantity())
                        .startTime(v.getStartTime())
                        .endTime(v.getEndTime())
                        .imageURL(v.getImageURL())
                        .build()).toList();

        return PaginationResponse.<VoucherResponse>builder()
                .content(voucherResponseList)
                .page(voucherPage.getNumber())
                .size(voucherPage.getSize())
                .totalElements(voucherPage.getTotalElements())
                .totalPage(voucherPage.getTotalPages())
                .build();
    }

    @Override
    public Set<VoucherResponse> getAllUserVouchers() {
        return voucherRepository.findAllVouchers().stream()
                .map(v -> VoucherResponse.builder()
                        .id(v.getId())
                        .discount(v.getDiscount())
                        .quantity(v.getQuantity())
                        .startTime(v.getStartTime())
                        .endTime(v.getEndTime())
                        .imageURL(v.getImageURL())
                        .build()).collect(Collectors.toSet());
    }

    @Override
    public void deleteVoucher(String id) throws IOException {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new CustomizedException(ErrorCode.VOUCHER_NOT_EXISTED));
        if (voucher.getImageId() != null)
            cloudinary.uploader().destroy(voucher.getImageId(), Map.of());
        voucherRepository.delete(voucher);
    }

    @Override
    public void updateVoucher(String id, UpdateVoucherRequest request) throws IOException {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new CustomizedException(ErrorCode.VOUCHER_NOT_EXISTED));
        if (request.getDiscount() != null) {
            voucher.setDiscount(request.getDiscount());
        }
        if (request.getQuantity() != null) {
            voucher.setQuantity(request.getQuantity());
        }
        if (request.getStartTime() != null) {
            voucher.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            voucher.setEndTime(request.getEndTime());
        }
        if (Objects.nonNull(request.getImage())) {
            if (voucher.getImageId() != null) {
                cloudinary.uploader().destroy(voucher.getImageId(), Map.of());
            }
            Map cloudinaryObject = cloudinary.uploader()
                    .upload(request.getImage().getBytes(), Map.of("folder", "bamito"));
            var imageUrl = cloudinaryObject.get("secure_url");
            var imageId = cloudinaryObject.get("public_id");
            voucher.setImageId(imageId.toString());
            voucher.setImageURL(imageUrl.toString());
        }
        voucherRepository.save(voucher);
    }
}
