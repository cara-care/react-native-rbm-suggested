require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-rbm-suggested"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  react-native-rbm-suggested
                   DESC
  s.homepage     = "https://github.com/cara-care/react-native-rbm-suggested"
  s.license      = "Copyright (c) Cara by HiDoc Technologies GmbH -- All Rights Reserved"
  s.authors      = { "Jan Czizikow" => "jan@cara.care", "Orest Tarasiuk" => "orest@cara.care", "Dankrad Feist" => "dankrad@cara.care" , "Mike Gerasymenko" => "mike@cara.care" }
  s.platforms    = { :ios => "9.0", :tvos => "10.0" }
  s.source       = { :git => "https://github.com/cara-care/react-native-rbm-suggested.git", :tag => "#{s.version}" }

  s.source_files = "ios/RbmSuggested-Bridging-Header.h", "ios/RBMConstants.swift", "ios/RBMSuggested.swift", "ios/RBMSuggested.m", "ios/contrib/YCMatrix/YCMatrix/Matrix.{h,m}", "ios/contrib/YCMatrix/YCMatrix/Matrix+Advanced.{h,m}", "ios/contrib/YCMatrix/YCMatrix/Matrix+Map.{h,m}", "ios/contrib/YCMatrix/YCMatrix/Matrix+Manipulate.{h,m}", "ios/contrib/YCMatrix/YCMatrix/Constants.h", "ios/HaltonInterface.{h,m}"
  s.resources = "android/src/main/assets/*"
  s.requires_arc = true

  s.dependency "React"
  s.swift_versions = ['5.0']
end

