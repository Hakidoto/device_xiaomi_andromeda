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

# Inherit some common Lineage stuff
$(call inherit-product, vendor/lineage/config/common_full_phone.mk)

# Inherit from andromeda device
$(call inherit-product, $(LOCAL_PATH)/device.mk)

# Device identifier. This must come after all inclusions.
PRODUCT_NAME := lineage_andromeda
PRODUCT_DEVICE := andromeda
PRODUCT_BRAND := Xiaomi
PRODUCT_MODEL := Mi MIX 3 5G
PRODUCT_MANUFACTURER := Xiaomi

# Set BUILD_FINGERPRINT variable to be picked up by both system and vendor build.prop
BUILD_FINGERPRINT := google/redfin/redfin:11/RQ1A.201205.010/6953398:user/release-keys

PRODUCT_BUILD_PROP_OVERRIDES += \
    PRIVATE_BUILD_DESC="andromeda-user-10-QKQ1.190825.002-20.1.7-release-keys" \
    PRODUCT_NAME="andromeda" \
    TARGET_DEVICE="andromeda"

PRODUCT_GMS_CLIENTID_BASE := android-xiaomi
