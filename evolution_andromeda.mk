#
# Copyright (C) 2020 The LineageOS Project
#
# SPDX-License-Identifier: Apache-2.0
#

# Inherit from those products. Most specific first.
$(call inherit-product, $(SRC_TARGET_DIR)/product/core_64_bit.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base_telephony.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/product_launched_with_p.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/updatable_apex.mk)

# Inherit some common Evolution X stuff
TARGET_BOOT_ANIMATION_RES := 1080
TARGET_GAPPS_ARCH := arm64
TARGET_INCLUDE_STOCK_ARCORE := true
TARGET_SUPPORTS_GOOGLE_RECORDER := true
WITH_GAPPS := true
$(call inherit-product, vendor/evolution/config/common_full_phone.mk)

# Inherit from andromeda device
$(call inherit-product, $(LOCAL_PATH)/device.mk)

# Device identifier. This must come after all inclusions.
PRODUCT_NAME := evolution_andromeda
PRODUCT_DEVICE := andromeda
PRODUCT_BRAND := Xiaomi
PRODUCT_MODEL := Mi MIX 3 5G
PRODUCT_MANUFACTURER := Xiaomi

# Set BUILD_FINGERPRINT variable to be picked up by both system and vendor build.prop
BUILD_FINGERPRINT := google/coral/coral:11/RQ2A.210505.002/7246365:user/release-keys

PRODUCT_BUILD_PROP_OVERRIDES += \
    PRIVATE_BUILD_DESC="coral-user 11 RQ2A.210505.002 7246365 release-keys" \
    PRODUCT_NAME="andromeda" \
    TARGET_DEVICE="andromeda"

PRODUCT_GMS_CLIENTID_BASE := android-xiaomi
