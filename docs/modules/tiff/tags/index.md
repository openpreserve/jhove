---
title: TIFF Tags
layout: page
---

{: #tags .section-heading}
# TIFF Tags

{: .table .table-striped}
| Tag | Tag Name | Type | Count | Definition | Note |
| --- | --- | --- | --- | --- | --- |
| 254 | NewSubFileType | LONG | 1   | TIFF |
| 255 | SubFileType | SHORT | 1   | TIFF |
| 256 | ImageWidth | SHORT / LONG | 1   | TIFF |
| 257 | ImageLength | SHORT / LONG | 1   | TIFF |
| 258 | BitsPerSample | SHORT | N   | TIFF |
| 259 | Compression | SHORT | 1   | TIFF |
| 262 | PhotometricInterpretation | SHORT | 1   | TIFF |
| 263 | Thresholding | SHORT | 1   | TIFF |
| 264 | CellWidth | SHORT | 1   | TIFF |
| 265 | CellLength | SHORT | 1   | TIFF |
| 266 | FillOrder | SHORT | 1   | TIFF |
| 269 | DocumentName | ASCII | N   | TIFF |
| 270 | ImageDescription | ASCII | N   | TIFF |
| 271 | Make | ASCII | N   | TIFF |
| 272 | Model | ASCII | N   | TIFF |
| 273 | StripOffsets | SHORT / LONG | N   | TIFF |
| 274 | Orientation | SHORT | 1   | TIFF |
| 277 | SamplesPerPixel | SHORT | 1   | TIFF |
| 278 | RowsPerStrip | SHORT / LONG | 1   | TIFF |
| 279 | StripByteCounts | SHORT / LONG | N   | TIFF |
| 280 | MinSampleValue | SHORT | N   | TIFF |
| 281 | MaxSampleValue | SHORT | N   | TIFF |
| 282 | XResolution | RATIONAL | 1   | TIFF |
| 283 | YResolution | RATIONAL | 1   | TIFF |
| 284 | PlanarConfiguration | SHORT | 1   | TIFF |
| 285 | PageName | ASCII | N   | TIFF |
| 286 | XPosition | RATIONAL | 1   | TIFF |
| 287 | YPosition | RATIONAL | 1   | TIFF |
| 288 | FreeOffsets | LONG | N   | TIFF |
| 289 | FreeByteCounts | LONG | N   | TIFF |
| 290 | GrayResponseUnit | SHORT | 1   | TIFF |
| 291 | GrayResponseCurve | SHORT | N   | TIFF |
| 292 | Group3Options | LONG | 1   | TIFF |
| 293 | Group4Options | LONG | 1   | TIFF |
| 296 | ResolutionUnit | SHORT | 1   | TIFF |
| 297 | PageNumber | SHORT | 2   | TIFF |
| 300 | ColorResponseUnit | SHORT | 1   | TIFF 4.0 |
| 301 | TransferFunction | SHORT | N   | TIFF |
| 305 | Software | ASCII | N   | TIFF |
| 306 | DateTime | ASCII | 20  | TIFF |
| 315 | Artist | ASCII | N   | TIFF |
| 316 | HostComputer | ASCII | N   | TIFF |
| 317 | Predictor | SHORT | 1   | TIFF |
| 318 | WhitePoint | RATIONAL | 2   | TIFF |
| 319 | PrimaryChromacities | RATIONAL | 6   | TIFF |
| 320 | ColorMap | SHORT | 3\*N | TIFF |
| 321 | HalftoneHints | SHORT | 2   | TIFF |
| 322 | TileWidth | SHORT / LONG | 1   | TIFF |
| 323 | TileLength | SHORT / LONG | 1   | TIFF |
| 324 | TileOffsets | LONG | N   | TIFF |
| 325 | TileByteCounts | SHORT / LONG | N   | TIFF |
| 326 | BadFaxLines | LONG | 1   | ClassF / RFC1314 |
| 327 | CleanFaxData | SHORT | 1   | ClassF / RFC1314 |
| 328 | ConsecutiveBadFaxLines | LONG | 1   | ClassF / RFC1314 |
| 330 | SubIFDs | LONG / IFD | N   | PageMaker |
| 332 | InkSet | SHORT | 1   | TIFF |
| 333 | InkNames | ASCII | N   | TIFF |
| 334 | NumberOfInks | SHORT | 1   | TIFF |
| 336 | DotRange | BYTE / SHORT | N   | TIFF |
| 337 | TargetPrinter | ASCII | N   | TIFF |
| 338 | ExtraSamples | BYTE | N   | TIFF |
| 339 | SampleFormat | SHORT | N   | TIFF |
| 340 | SMinSampleValue | Any | N   | TIFF |
| 341 | SMaxSampleValue | Any | N   | TIFF |
| 342 | TransferRange | SHORT | 6   | TIFF |
| 343 | ClipPath | BYTE | N   | PageMaker |
| 344 | XClipPathUnits | LONG | 1   | PageMaker |
| 345 | YClipPathUnits | LONG | 1   | PageMaker |
| 346 | Indexed | SHORT | 1   | PageMaker |
| 351 | OPIProxy | SHORT | 1   | PageMaker |
| 437 | JPEG tables | UNDEFINED | N   | Photoshop |
| 512 | JPEGProc | SHORT | 1   | TIFF |
| 513 | JPEGInterchangeFormat | LONG | 1   | TIFF |
| 514 | JPEGInterchangeFormatLength | LONG | 1   | TIFF |
| 515 | JPEGRestartInterval | SHORT | 1   | TIFF |
| 517 | JPEGLosslessPredictors | SHORT | N   | TIFF |
| 518 | JPEGPointTransforms | SHORT | N   | TIFF |
| 519 | JPEGQTables | LONG | N   | TIFF |
| 520 | JPEGDCTables | LONG | N   | TIFF |
| 521 | JPEGACTables | LONG | N   | TIFF |
| 529 | YCbCrCoefficients | RATIONAL | 3   | TIFF |
| 530 | YCbCrSubsampling | SHORT | 2   | TIFF |
| 531 | YCbCrPositioning | SHORT | 1   | TIFF |
| 532 | ReferenceBlackWhite | LONG | 2\*N | TIFF |
| 700 | XMP | BYTE | N   | XMP |
| 32781 | ImageID | ASCII | N   | PageMaker |
| 32995 | Matteing | \-  | \-  | \-  | deprecated; use ExtraSamples |
| 32996 | DataType | \-  | \-  | \-  | deprecated; use SampleFormat |
| 32997 | ImageDepth | \-  | \-  | \-  |
| 32998 | TileDepth | \-  | \-  | \-  |
| 33421 | CFARepeatPatternDim | SHORT | 2   | TIFF/EP |
| 33422 | CFAPattern | BYTE | N   | TIFF/EP |
| 33423 | BatteryLevel | RATIONAL / ASCII | 1   | TIFF/EP |
| 33432 | Copyright | ASCII | any | TIFF/EP |
| 33434 | ExposureTime | RATIONAL | 1   | TIFF/EP |
| 33437 | Fnumber | RATIONAL | N   | TIFF/EP |
| 33723 | IPTC/NAA | LONG / ASCII |     | TIFF/EP |
| 33550 | ModelPixelScaleTag | DOUBLE | 3   | GeoTIFF |
| 33920 | IntergraphMatrixTag | DOUBLE | 17  | GeoTIFF | deprecated |
| 33922 | ModelTiepointTag | DOUBLE | 6\*N | GeoTIFF |
| 34016 | Site | ASCII | N   | TIFF/IT |
| 34017 | ColorSequence | ASCII | N   | TIFF/IT |
| 34018 | IT8Header | ASCII | N   | TIFF/IT |
| 34019 | RasterPadding | SHORT | 1   | TIFF/IT |
| 34020 | BitsPerRunLength | SHORT | 1   | TIFF/IT |
| 34021 | BitsPerExtendedRunLength | SHORT | 1   | TIFF/IT |
| 34022 | ColorTable | BYTE | N   | TIFF/IT |
| 34023 | ImageColorIndicator | BYTE | 1   | TIFF/IT |
| 34024 | BackgroundColorIndicator | BYTE | 1   | TIFF/IT |
| 34025 | ImageColorValue | BYTE | 1   | TIFF/IT |
| 34026 | BackgroundColorValue | BYTE | 1   | TIFF/IT |
| 34027 | PixelInensityRange | BYTE | 2   | TIFF/IT |
| 34028 | TransparencyIndicator | BYTE | 1   | TIFF/IT |
| 34029 | ColorCharacterization | ASCII | N   | TIFF/IT |
| 34030 | HCUsage | LONG | 1   | TIFF/IT |
| 34264 | ModelTransformationTag | DOUBLE | 16  | GeoTIFF |
| 34377 | PhotoshopImageResources | UNDEFINED |     | Photoshop |
| 34665 | ExifIFD | LONG | 1   | Exif |
| 34675 | InterColourProfile | UNDEFINED | N   | TIFF/EP |
| 34732 | ImageLayer | SHORT or LONG | 2   | RFC 2301 |
| 34735 | GeoKeyDirectoryTag | SHORT | 4\*N | GeoTIFF |
| 34736 | GeoDoubleParamsTag | DOUBLE | N   | GeoTIFF |
| 34737 | GeoAsciiParamsTag | ASCII | N   | GeoTIFF |
| 34850 | ExposureProgram | SHORT | 1   | TIFF/EP |
| 34852 | SpectralSensitivity | ASCII | N   | TIFF/EP |
| 34853 | GPSInfo | LONG | 1   | TIFF/EP / Exif |
| 34855 | ISOSpeedRatings | SHORT | 3   | TIFF/EP |
| 34856 | OECF | UNDEFINED | N   | TIFF/EP |
| 34857 | Interlace | SHORT | 1   | TIFF/EP |
| 34858 | TimeZoneOffset | SSHORT | N   | TIFF/EP |
| 34859 | SelfTimerMode | SHORT | 1   | TIFF/EP |
| 34908 | FaxRecvParams | \-  | \-  | \-  |
| 34909 | FaxSubAddress | \-  | \-  | \-  |
| 34910 | FaxRecvTime | \-  | \-  | \-  |
| 36867 | DateTimeOriginal | ASCII | 20  | TIFF/EP |
| 37122 | CompressedBitsPerPixel | RATIONAL | 1   | TIFF/EP |
| 37377 | ShutterSpeedValue | RATIONAL | 1   | TIFF/EP |
| 37378 | ApertureValue | RATIONAL | 1   | TIFF/EP |
| 37379 | BrightnessValue | SRATIONAL | N   | TIFF/EP |
| 37380 | ExposureBiasValue | SRATIONAL | N   | TIFF/EP |
| 37381 | MaxApertureValue | RATIONAL | 1   | TIFF/EP |
| 37382 | SubjectDistance | SRATIONAL | N   | TIFF/EP |
| 37383 | MeteringMode | SHORT | 1   | TIFF/EP |
| 37384 | LightSource | SHORT | 1   | TIFF/EP |
| 37385 | Flash | SHORT | 1   | TIFF/EP |
| 37386 | FocalLength | RATIONAL | N   | TIFF/EP |
| 37387 | FlashEnergy | RATIONAL | N   | TIFF/EP |
| 37388 | SpatialFrequencyResponse | UNDEFINED | N   | TIFF/EP |
| 37389 | Noise | UNDEFINED | N   | TIFF/EP |
| 37390 | FocalPlaneXResolution | RATIONAL | 1   | TIFF/EP |
| 37391 | FocalPlaneYResolution | RATIONAL | 1   | TIFF/EP |
| 37392 | FocalPlaneResolutionUnit | SHORT | 1   | TIFF/EP |
| 37393 | ImageNumber | LONG | 1   | TIFF/EP |
| 37394 | SecurityClassification | ASCII | N   | TIFF/EP |
| 37395 | ImageHistory | ASCII | N   | TIFF/EP |
| 37396 | SubjectLocation | SHORT | N   | TIFF/EP |
| 37397 | ExposureIndex | RATIONAL | N   | TIFF/EP |
| 37398 | TIFF/EPStandardID | BYTE | 4   | TIFF/EP |
| 37399 | SensingMethod | SHORT | 1   | TIFF/EP |
| 37439 | StoNits | \-  | \-  | \-  |
| 37724 | ImageSourceData | UNDEFINED | N   | Photoshop |
| 40965 | InteroperabilityIFD | LONG | 1   | Exif |
| 50255 | PhotoshopAnnotations | UNDEFINED |     | Photoshop |
| 50706 | DNGVersion | BYTE | 4   | DNG |
| 50707 | DNGBackwardVersion | BYTE | 4   | DNG |
| 50708 | UniqueCameraModel | ASCII | N   | DNG |
| 50709 | LocalizedCameraModel | BYTE | N   | DNG |
| 50710 | CFAPlaneColor | BYTE | N   | DNG |
| 50711 | CFALayout | Short | 1   | DNG |
| 50712 | LinearizationTable | SHORT | N   | DNG |
| 50713 | BlackLevelRepeatDim | SHORT | 2   | DNG |
| 50714 | BlackLevel | RATIONAL | N   | DNG |
| 50715 | BlackLevelDeltaH | SRATIONAL | N   | DNG |
| 50716 | BlackLevelDeltaV | SRATIONAL | N   | DNG |
| 50717 | WhiteLevel | LONG | N   | DNG |
| 50718 | DefaultScale | RATIONAL | 2   | DNG |
| 50719 | DefaultCropOrigin | RATIONAL | 2   | DNG |
| 50720 | DefaultCropSize | RATIONAL | 4   | DNG |
| 50721 | ColorMatrix1 | SRATIONAL | 3\*N | DNG |
| 50722 | ColorMatrix2 | SRATIONAL | 3\*N | DNG |
| 50723 | CameraCalibration1 | SRATIONAL | N\*N | DNG |
| 50724 | CameraCalibration2 | SRATIONAL | N\*N | DNG |
| 50725 | ReductionMatrix1 | SRATIONAL | 3\*N | DNG |
| 50726 | ReductionMatrix2 | SRATIONAL | 3\*N | DNG |
| 50727 | AnalogBalnace | RATIONAL | N   | DNG |
| 50728 | AsShortNeutral | RATIONAL | N   | DNG |
| 50729 | AsShortWhiteXY | RATIONAL | 2   | DNG |
| 50730 | BaselineExposure | RATIONAL | 1   | DNG |
| 50731 | BaselineNoise | RATIONAL | 1   | DNG |
| 50732 | BaselineSharpness | RATIONAL | 1   | DNG |
| 50733 | BayerGreenSplit | LONG | 1   | DNG |
| 50734 | LinearResponseLimit | RATIONAL | 1   | DNG |
| 50735 | CameraSerialNumber | ASCII | N   | DNG |
| 50736 | LensInfo | RATIONAL | 4   | DNG |
| 50737 | ChromaBlurRadius | RATIONAL | 1   | DNG |
| 50738 | AntiAliasStrength | RATIONAL | 1   | DNG |
| 50740 | DNGPrivateDatea | BYTE | N   | DNG |
| 50741 | MakerNoteSafety | SHORT | 1   | DNG |
| 50778 | CalibrationIlluminant1 | SHORT | 1   | DNG |
| 50779 | CalibrationIlluminant2 | SHORT | 1   | DNG |
| 50780 | BestQualityScale | RATIONAL | 1   | DNG |

{: #gps .section-heading}
**2 GPSInfo IFD Tags**

{: .table .table-striped}
| Tag | Tag Name | Type | Count | Definition |
| --- | --- | --- | --- | --- |
| 0   | GPSVersionID | BYTE | 4   | TIFF/EP / Exif |
| 1   | GPSLatitudeRef | ASCII | 2   | TIFF/EP / Exif |
| 2   | GPSLatitude | RATIONAL | 3   | TIFF/EP / Exif |
| 3   | GPSLongitudeRef | ASCII | 2   | TIFF/EP / Exif |
| 4   | GPSLongitude | RATIONAL | 3   | TIFF/EP / Exif |
| 5   | GPSAltitudeRef | BYTE | 1   | TIFF/EP / Exif |
| 6   | GPSAltitude | RATAIONAL | 1   | TIFF/EP / Exif |
| 7   | GPSTimeStamp | RATIONAL | 3   | TIFF/EP / Exif |
| 8   | GPSSatellites | ASCII | N   | TIFF/EP / Exif |
| 9   | GPSStatus | ASCII | 2   | TIFF/EP / Exif |
| 10  | GPSMeasureMode | ASCII | 2   | TIFF/EP / Exif |
| 11  | GPSDOP | RATIONAL | 1   | TIFF/EP / Exif |
| 12  | GPSSpeedRef | ASCII | 2   | TIFF/EP / Exif |
| 13  | GPSSpeed | RATIONAL | 1   | TIFF/EP / Exif |
| 14  | GPSTrackRef | ASCII | 2   | TIFF/EP / Exif |
| 15  | GPSTrack | RATIONAL | 1   | TIFF/EP / Exif |
| 16  | GPSImgDirectionRef | ASCII | 2   | TIFF/EP / Exif |
| 17  | GPSImgDirection | RATIONAL | 1   | TIFF/EP / Exif |
| 18  | GPSMapDatum | ASCII | N   | TIFF/EP / Exif |
| 19  | GPSDestLatitudeRef | ASCII | 2   | TIFF/EP / Exif |
| 20  | GPSDestLatitude | RATIONAL | 3   | TIFF/EP / Exif |
| 21  | GPSDestLongitudeRef | ASCII | 2   | TIFF/EP / Exif |
| 22  | GPSDestLongitude | RATIONAL | 3   | TIFF/EP / Exif |
| 23  | GPSDestBearingRef | ASCII | 2   | TIFF/EP / Exif |
| 24  | GPSDestBearing | RATIONAL | 1   | TIFF/EP / Exif |
| 25  | GPSDestDistanceRef | ASCII | 2   | TIFF/EP / Exif |
| 26  | GPSDestDistance | RATIONAL | 1   | TIFF/EP / Exif |
| 27  | GPSProcessingMethod | UNDEFINED | N   | Exif |
| 28  | GPSAreaInformation | UNDEFINED | N   | Exif |
| 29  | GPSDateStamp | ASCII | 11  | Exif |
| 30  | GPSDifferential | SHORT | 1   | Exif |

{: #exif .section-heading}
**3 Exif IFD Tags**

{: .table .table-striped}
| Tag | Tag Name | Type | Count | Definition |
| --- | --- | --- | --- | --- | --- |
| 36864 | ExifVersion | UNDEFINED | 4   | Exif |
| 36867 | DateTimeOriginal | ASCII | 20  | Exif |
| 36868 | DateTimeDigitized | ASCII | 20  | Exif |
| 33434 | ExposureTime | RATIONAL | 1   | Exif |
| 33437 | FNumber | RATIONAL | 1   | Exif |
| 34850 | ExposureProgram | SHORT | 1   | Exif |
| 34852 | SpectralSensitibity | ASCII | N   | Exif |
| 34855 | ISOSpeedRatings | SHORT | N   | Exif |
| 34856 | OECF | UNDEFINED | N   | Exif |
| 37121 | ComponentsConfiguration | UNDEFINED | 4   | Exif |
| 37122 | CompressedBitsPerPixel | RATIONAL | 1   | Exif |
| 37377 | ShutterSpeedValue | SRATIONAL | 1   | Exif |
| 37378 | ApertureValue | RATIONAL | 1   | Exif |
| 37379 | BrightnessValue | SRATIONAL | 1   | Exif |
| 37380 | ExposureBiasValue | SRATIONAL | 1   | Exif |
| 37381 | MaxApertureValue | RATIONAL | 1   | Exif |
| 37382 | SubjectDistance | RATIONAL | 1   | Exif |
| 37383 | MeteringMode | SHORT | 1   | Exif |
| 37384 | LightSource | SHORT | 1   | Exif |
| 37385 | Flash | SHORT | 1   | Exif |
| 37386 | FocalLength | RATIONAL | 1   | Exif |
| 37396 | SubjectArea | SHORT | N   | Exif |
| 37500 | MakerNote | UNDEFINED | N   | Exif |
| 37510 | UserComment | UNDEFIEND | N   | Exif |
| 37520 | SubSecTime | ASCII | N   | Exif |
| 37521 | SubSecTimeOriginal | ASCII | N   | Exif |
| 37522 | SubSecTimeDigitized | ASCII | N   | Exif |
| 40960 | FlashpixVersion | UNDEFINED | 4   | Exif |
| 40961 | ColorSpace | SHORT | 1   | Exif |
| 40962 | PixelXDimension | SHORT/LONG | 1   | Exif |
| 40963 | PixelYDimension | SHORT/LONG | 1   | Exif |
| 40964 | RelatedSoundFile | ASCII | 13  | Exif |
| 41483 | FlashEnergy | RATIONAL | 1   | Exif |
| 41484 | SpatialFrequencyResponse | UNDEFINED | N   | Exif |
| 41486 | FocalPlaneXResolution | RATIONAL | 1   | Exif |
| 41487 | FocalPlaneYResolution | RATIONAL | 1   | Exif |
| 41488 | FocalPlaneResolutionUnit | SHORT | 1   | Exif |
| 41492 | SubjectLocation | SHORT | 2   | Exif |
| 41493 | ExposureIndex | RATIONAL | 1   | Exif |
| 41495 | SensingMethod | SHORT | 1   | Exif |
| 41728 | FileSource | UNDEFINED | 1   | Exif |
| 41729 | SceneType | UNDEFINED | 1   | Exif |
| 41730 | CFAPattern | UNDEFINED | N   | Exif |
| 41985 | CustomRendered | SHORT | 1   | Exif |
| 41986 | ExposureMode | SHORT | 1   | Exif |
| 41987 | WhiteBalance | SHORT | 1   | Exif |
| 41988 | DigitalZoomRatio | RATIONAL | 1   | Exif |
| 41989 | FocalLengthIn35mmFilm | SHORT | 1   | Exif |
| 41990 | SceneCaptureType | SHORT | 1   | Exif |
| 41991 | GainControl | SHORT | 1   | Exif | The EXIF specification (JEITA CP-3451, April 2002) incorrectly identifies the type as RATIONAL in Table 5 (p. 25); it is correctly identified as SHORT on page 43 |
| 41992 | Contrast | SHORT | 1   | Exif |
| 41993 | Saturation | SHORT | 1   | Exif |
| 41994 | Sharpness | SHORT | 1   | Exif |
| 41995 | DeviceSettingDescription | UNDEFINED | N   | Exif |
| 41996 | SubjectDistanceRange | SHORT | 1   | Exif |

{: #interoperability .section-heading}
**4 Exif Interoperability IFD Tags**

{: .table .table-striped}
| Tag | Tag Name | Type | Count | Definition |
| --- | --- | --- | --- | --- |
| 1   | InteroperabilityIndex | ASCII | N   | Exif |

{: #compression .section-heading}
**5 TIFF Compression Schemes**

{: .table .table-striped}
|     |     |     |
| --- | --- | --- |
| 1   | Uncompressed | TIFF |
| 2   | CCITT 1D | TIFF |
| 3   | CCITT Group 3 | TIFF |
| 4   | CCITT Group 4 | TIFF |
| 5   | LZW | TIFF |
| 6   | JPEG | TIFF |
| 7   | JPEG | Photoshop |
| 8   | Deflate | Photoshop |
| 32766 | NeXT 2-bit encoding |
| 32771 | CCITT RLE | libTiff |
| 32773 | PackBits | TIFF |
| 32809 | ThunderScan 4-bit encoding |
| 32895 | RasterPadding in CT or MP | TIFF/IT |
| 32896 | RLE for LW | TIFF/IT |
| 32897 | RLE for HC | TIFF/IT |
| 32898 | RLE for BL | TIFF/IT |
| 32908 | Pixar 10-bit LZW | libTiff |
| 32909 | Pixar companded 11-bit ZIP encoding | libTiff |
| 32946 | PKZIP-style Deflate encoding (experimental) |
| 32947 | Kodak DCS |
| 34661 | JBIG | libTiff |
| 34676 | SGI 32-bit Log Luminance encoding (experimental) |
| 34677 | SGI 24-bit Log Luminance encoding (experimental) |
| 34712 | JPEG 2000 | libTiff |

{: #photointerp .section-heading}
**6 TIFF PhotometricInterpretation Schemes**

{: .table .table-striped}
|     |     |     |
| --- | --- | --- |
| 0   | White is zero | TIFF |
| 1   | Black is zero | TIFF |
| 2   | RGB | TIFF |
| 3   | Palette | TIFF |
| 4   | Transparency mask | TIFF |
| 5   | CMYK | TIFF |
| 6   | YCbCr | TIFF |
| 8   | CIE L\*a\*b\* | TIFF |
| 9   | ICC L\*a\*b\* | Photoshop |
| 10  | ITU L\*a\*b\* |     |
| 32803 | CFA |     |
| 32844 | CIE Log2(L) |     |
| 32845 | CIE Log2(L)(u',v') |     |
| 34892 | LinearRaw | DNG |
