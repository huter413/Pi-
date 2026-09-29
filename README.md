# Pi Emoji Skin

Android IME project for a custom ninja character.

## What changed

- The keyboard has a dedicated ninja key.
- The PNG selected in the app is copied into the app's private files and is used as the ninja key image.
- When a text field accepts rich input content, the keyboard offers the selected PNG as image/png.
- Otherwise the keyboard inserts U+E000, a Private Use Area character.
- tools/build_ninja_font.py converts ninja_sample.png into app/src/main/res/font/pi_ninja.ttf during GitHub Actions.
- The generated font maps U+E000 to the ninja artwork.

## Important font behavior

U+E000 is not a Unicode emoji. It is a Private Use Area character. The Pi Ninja font is bundled in the APK, but another app such as YouTube does not automatically start using that font. For the ninja glyph to render as the artwork in normal text, the receiving renderer must use the Pi Ninja font. Apps that do not use it may show an empty/tofu private-use glyph.

## Build

GitHub Actions generates the font from app/src/main/res/drawable-nodpi/ninja_sample.png and then builds the APK.
