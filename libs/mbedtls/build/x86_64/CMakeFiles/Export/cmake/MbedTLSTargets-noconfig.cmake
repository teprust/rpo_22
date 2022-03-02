#----------------------------------------------------------------
# Generated CMake target import file.
#----------------------------------------------------------------

# Commands may need to know the format version.
set(CMAKE_IMPORT_FILE_VERSION 1)

# Import target "MbedTLS::mbedcrypto" for configuration ""
set_property(TARGET MbedTLS::mbedcrypto APPEND PROPERTY IMPORTED_CONFIGURATIONS NOCONFIG)
set_target_properties(MbedTLS::mbedcrypto PROPERTIES
  IMPORTED_LOCATION_NOCONFIG "${_IMPORT_PREFIX}/lib/libmbedcrypto.so"
  IMPORTED_SONAME_NOCONFIG "libmbedcrypto.so"
  )

list(APPEND _IMPORT_CHECK_TARGETS MbedTLS::mbedcrypto )
list(APPEND _IMPORT_CHECK_FILES_FOR_MbedTLS::mbedcrypto "${_IMPORT_PREFIX}/lib/libmbedcrypto.so" )

# Import target "MbedTLS::mbedx509" for configuration ""
set_property(TARGET MbedTLS::mbedx509 APPEND PROPERTY IMPORTED_CONFIGURATIONS NOCONFIG)
set_target_properties(MbedTLS::mbedx509 PROPERTIES
  IMPORTED_LOCATION_NOCONFIG "${_IMPORT_PREFIX}/lib/libmbedx509.so"
  IMPORTED_SONAME_NOCONFIG "libmbedx509.so"
  )

list(APPEND _IMPORT_CHECK_TARGETS MbedTLS::mbedx509 )
list(APPEND _IMPORT_CHECK_FILES_FOR_MbedTLS::mbedx509 "${_IMPORT_PREFIX}/lib/libmbedx509.so" )

# Import target "MbedTLS::mbedtls" for configuration ""
set_property(TARGET MbedTLS::mbedtls APPEND PROPERTY IMPORTED_CONFIGURATIONS NOCONFIG)
set_target_properties(MbedTLS::mbedtls PROPERTIES
  IMPORTED_LOCATION_NOCONFIG "${_IMPORT_PREFIX}/lib/libmbedtls.so"
  IMPORTED_SONAME_NOCONFIG "libmbedtls.so"
  )

list(APPEND _IMPORT_CHECK_TARGETS MbedTLS::mbedtls )
list(APPEND _IMPORT_CHECK_FILES_FOR_MbedTLS::mbedtls "${_IMPORT_PREFIX}/lib/libmbedtls.so" )

# Import target "MbedTLS::mbedcrypto_static" for configuration ""
set_property(TARGET MbedTLS::mbedcrypto_static APPEND PROPERTY IMPORTED_CONFIGURATIONS NOCONFIG)
set_target_properties(MbedTLS::mbedcrypto_static PROPERTIES
  IMPORTED_LINK_INTERFACE_LANGUAGES_NOCONFIG "C"
  IMPORTED_LOCATION_NOCONFIG "${_IMPORT_PREFIX}/lib/libmbedcrypto.a"
  )

list(APPEND _IMPORT_CHECK_TARGETS MbedTLS::mbedcrypto_static )
list(APPEND _IMPORT_CHECK_FILES_FOR_MbedTLS::mbedcrypto_static "${_IMPORT_PREFIX}/lib/libmbedcrypto.a" )

# Import target "MbedTLS::mbedx509_static" for configuration ""
set_property(TARGET MbedTLS::mbedx509_static APPEND PROPERTY IMPORTED_CONFIGURATIONS NOCONFIG)
set_target_properties(MbedTLS::mbedx509_static PROPERTIES
  IMPORTED_LINK_INTERFACE_LANGUAGES_NOCONFIG "C"
  IMPORTED_LOCATION_NOCONFIG "${_IMPORT_PREFIX}/lib/libmbedx509.a"
  )

list(APPEND _IMPORT_CHECK_TARGETS MbedTLS::mbedx509_static )
list(APPEND _IMPORT_CHECK_FILES_FOR_MbedTLS::mbedx509_static "${_IMPORT_PREFIX}/lib/libmbedx509.a" )

# Import target "MbedTLS::mbedtls_static" for configuration ""
set_property(TARGET MbedTLS::mbedtls_static APPEND PROPERTY IMPORTED_CONFIGURATIONS NOCONFIG)
set_target_properties(MbedTLS::mbedtls_static PROPERTIES
  IMPORTED_LINK_INTERFACE_LANGUAGES_NOCONFIG "C"
  IMPORTED_LOCATION_NOCONFIG "${_IMPORT_PREFIX}/lib/libmbedtls.a"
  )

list(APPEND _IMPORT_CHECK_TARGETS MbedTLS::mbedtls_static )
list(APPEND _IMPORT_CHECK_FILES_FOR_MbedTLS::mbedtls_static "${_IMPORT_PREFIX}/lib/libmbedtls.a" )

# Commands beyond this point should not need to know the version.
set(CMAKE_IMPORT_FILE_VERSION)
