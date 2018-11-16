# language: tr

@Category
Özellik: Sisteme yeni kategori eklenmesi

  Senaryo: 1 - Kategori yoneticisinin sisteme basirili bir sekilde yeni bir kategori eklemesi
    Diyelim ki Kategori yoneticisi sisteme yeni bir kategori eklemek istiyor
    Eğer ki Kategori yoneticisi eklenecek kategori icin bir isim girdi ise
    Ve Daha once sistemde olusturulmus ayni isimli bir kategori mevcut degil ise
    Ve Kategorinin tanimli ust kategorileri de sistemde mevcut kategoriler ise
    O zaman Sistem yeni kategoriyi kaydeder

  Senaryo: 2 - Kategori yoneticisinin kategori ismini bos birakarak yeni bir kategori eklememeyi denemesi
    Diyelim ki Kategori yoneticisi sisteme kategori ismi mevcut olmayan yeni bir kategori eklemek istiyor
    O zaman Sistem kategoriyi kaydetmez ve kategori yoneticisine kategori ismi bos birakilamaz gibi bir hata mesaji iletir

  Senaryo: 3 - Kategori yoneticisinin sistemde zaten mevcut olan bir isimle kategori olusturmayi denemesi
    Diyelim ki Kategori yoneticisi sisteme daha once sistemde mevcut olan bir isimle yeni bir kategori eklemek istiyor
    O zaman Sistem kategoriyi kaydetmez ve kategori yoneticisine kategori zaten sistemde mevcut gibi bir hata mesaji iletir

  Senaryo: 4 - Kategori yoneticisi sisteme herhangi bir ust kategorisi sistemde olmayan bir kategori eklemeye calisirsa
    Diyelim ki Kategori yoneticisi sisteme ust kategorilerinden biri sistemde olmayan yeni bir kategori eklemek istiyor
    O zaman Sistem kategoriyi kaydetmez ve kategori yoneticisine gecersiz ust kategori bilgisi bir hata mesaji iletir



