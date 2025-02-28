
package com.cydeo.dto;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "1000sats",
    "1inch",
    "aave",
    "ada",
    "aed",
    "afn",
    "agix",
    "akt",
    "algo",
    "all",
    "amd",
    "amp",
    "ang",
    "aoa",
    "ape",
    "apt",
    "ar",
    "arb",
    "ars",
    "atom",
    "ats",
    "aud",
    "avax",
    "awg",
    "axs",
    "azm",
    "azn",
    "bake",
    "bam",
    "bat",
    "bbd",
    "bch",
    "bdt",
    "bef",
    "bgn",
    "bhd",
    "bif",
    "bmd",
    "bnb",
    "bnd",
    "bob",
    "brl",
    "bsd",
    "bsv",
    "bsw",
    "btc",
    "btcb",
    "btg",
    "btn",
    "btt",
    "busd",
    "bwp",
    "byn",
    "byr",
    "bzd",
    "cad",
    "cake",
    "cdf",
    "celo",
    "cfx",
    "chf",
    "chz",
    "clp",
    "cnh",
    "cny",
    "comp",
    "cop",
    "crc",
    "cro",
    "crv",
    "cspr",
    "cuc",
    "cup",
    "cve",
    "cvx",
    "cyp",
    "czk",
    "dai",
    "dash",
    "dcr",
    "dem",
    "dfi",
    "djf",
    "dkk",
    "doge",
    "dop",
    "dot",
    "dydx",
    "dzd",
    "eek",
    "egld",
    "egp",
    "enj",
    "eos",
    "ern",
    "esp",
    "etb",
    "etc",
    "eth",
    "eur",
    "fei",
    "fil",
    "fim",
    "fjd",
    "fkp",
    "flow",
    "flr",
    "frax",
    "frf",
    "ftm",
    "ftt",
    "fxs",
    "gala",
    "gbp",
    "gel",
    "ggp",
    "ghc",
    "ghs",
    "gip",
    "gmd",
    "gmx",
    "gnf",
    "gno",
    "grd",
    "grt",
    "gt",
    "gtq",
    "gusd",
    "gyd",
    "hbar",
    "hkd",
    "hnl",
    "hnt",
    "hot",
    "hrk",
    "ht",
    "htg",
    "huf",
    "icp",
    "idr",
    "iep",
    "ils",
    "imp",
    "imx",
    "inj",
    "inr",
    "iqd",
    "irr",
    "isk",
    "itl",
    "jep",
    "jmd",
    "jod",
    "jpy",
    "kas",
    "kava",
    "kcs",
    "kda",
    "kes",
    "kgs",
    "khr",
    "klay",
    "kmf",
    "knc",
    "kpw",
    "krw",
    "ksm",
    "kwd",
    "kyd",
    "kzt",
    "lak",
    "lbp",
    "ldo",
    "leo",
    "link",
    "lkr",
    "lrc",
    "lrd",
    "lsl",
    "ltc",
    "ltl",
    "luf",
    "luna",
    "lunc",
    "lvl",
    "lyd",
    "mad",
    "mana",
    "matic",
    "mbx",
    "mdl",
    "mga",
    "mgf",
    "mina",
    "mkd",
    "mkr",
    "mmk",
    "mnt",
    "mop",
    "mro",
    "mru",
    "mtl",
    "mur",
    "mvr",
    "mwk",
    "mxn",
    "mxv",
    "myr",
    "mzm",
    "mzn",
    "nad",
    "near",
    "neo",
    "nexo",
    "nft",
    "ngn",
    "nio",
    "nlg",
    "nok",
    "npr",
    "nzd",
    "okb",
    "omr",
    "one",
    "op",
    "ordi",
    "pab",
    "paxg",
    "pen",
    "pepe",
    "pgk",
    "php",
    "pkr",
    "pln",
    "pte",
    "pyg",
    "qar",
    "qnt",
    "qtum",
    "rol",
    "ron",
    "rpl",
    "rsd",
    "rub",
    "rune",
    "rvn",
    "rwf",
    "sand",
    "sar",
    "sbd",
    "scr",
    "sdd",
    "sdg",
    "sek",
    "sgd",
    "shib",
    "shp",
    "sit",
    "skk",
    "sle",
    "sll",
    "snx",
    "sol",
    "sos",
    "spl",
    "srd",
    "srg",
    "std",
    "stn",
    "stx",
    "sui",
    "svc",
    "syp",
    "szl",
    "thb",
    "theta",
    "tjs",
    "tmm",
    "tmt",
    "tnd",
    "ton",
    "top",
    "trl",
    "trx",
    "try",
    "ttd",
    "tusd",
    "tvd",
    "twd",
    "twt",
    "tzs",
    "uah",
    "ugx",
    "uni",
    "usd",
    "usdc",
    "usdd",
    "usdp",
    "usdt",
    "uyu",
    "uzs",
    "val",
    "veb",
    "ved",
    "vef",
    "ves",
    "vet",
    "vnd",
    "vuv",
    "waves",
    "wemix",
    "woo",
    "wst",
    "xaf",
    "xag",
    "xau",
    "xaut",
    "xbt",
    "xcd",
    "xch",
    "xdc",
    "xdr",
    "xec",
    "xem",
    "xlm",
    "xmr",
    "xof",
    "xpd",
    "xpf",
    "xpt",
    "xrp",
    "xtz",
    "yer",
    "zar",
    "zec",
    "zil",
    "zmk",
    "zmw",
    "zwd",
    "zwg",
    "zwl"
})
@Generated("jsonschema2pojo")
public class Usd {

    @JsonProperty("1000sats")
    private Double _1000sats;
    @JsonProperty("1inch")
    private Double _1inch;
    @JsonProperty("aave")
    private Double aave;
    @JsonProperty("ada")
    private Double ada;
    @JsonProperty("aed")
    private Double aed;
    @JsonProperty("afn")
    private Double afn;
    @JsonProperty("agix")
    private Double agix;
    @JsonProperty("akt")
    private Double akt;
    @JsonProperty("algo")
    private Double algo;
    @JsonProperty("all")
    private Double all;
    @JsonProperty("amd")
    private Double amd;
    @JsonProperty("amp")
    private Double amp;
    @JsonProperty("ang")
    private Double ang;
    @JsonProperty("aoa")
    private Double aoa;
    @JsonProperty("ape")
    private Double ape;
    @JsonProperty("apt")
    private Double apt;
    @JsonProperty("ar")
    private Double ar;
    @JsonProperty("arb")
    private Double arb;
    @JsonProperty("ars")
    private Double ars;
    @JsonProperty("atom")
    private Double atom;
    @JsonProperty("ats")
    private Double ats;
    @JsonProperty("aud")
    private Double aud;
    @JsonProperty("avax")
    private Double avax;
    @JsonProperty("awg")
    private Double awg;
    @JsonProperty("axs")
    private Double axs;
    @JsonProperty("azm")
    private Double azm;
    @JsonProperty("azn")
    private Double azn;
    @JsonProperty("bake")
    private Double bake;
    @JsonProperty("bam")
    private Double bam;
    @JsonProperty("bat")
    private Double bat;
    @JsonProperty("bbd")
    private Integer bbd;
    @JsonProperty("bch")
    private Double bch;
    @JsonProperty("bdt")
    private Double bdt;
    @JsonProperty("bef")
    private Double bef;
    @JsonProperty("bgn")
    private Double bgn;
    @JsonProperty("bhd")
    private Double bhd;
    @JsonProperty("bif")
    private Double bif;
    @JsonProperty("bmd")
    private Integer bmd;
    @JsonProperty("bnb")
    private Double bnb;
    @JsonProperty("bnd")
    private Double bnd;
    @JsonProperty("bob")
    private Double bob;
    @JsonProperty("brl")
    private Double brl;
    @JsonProperty("bsd")
    private Integer bsd;
    @JsonProperty("bsv")
    private Double bsv;
    @JsonProperty("bsw")
    private Double bsw;
    @JsonProperty("btc")
    private Double btc;
    @JsonProperty("btcb")
    private Double btcb;
    @JsonProperty("btg")
    private Double btg;
    @JsonProperty("btn")
    private Double btn;
    @JsonProperty("btt")
    private Double btt;
    @JsonProperty("busd")
    private Double busd;
    @JsonProperty("bwp")
    private Double bwp;
    @JsonProperty("byn")
    private Double byn;
    @JsonProperty("byr")
    private Double byr;
    @JsonProperty("bzd")
    private Double bzd;
    @JsonProperty("cad")
    private Double cad;
    @JsonProperty("cake")
    private Double cake;
    @JsonProperty("cdf")
    private Double cdf;
    @JsonProperty("celo")
    private Double celo;
    @JsonProperty("cfx")
    private Double cfx;
    @JsonProperty("chf")
    private Double chf;
    @JsonProperty("chz")
    private Double chz;
    @JsonProperty("clp")
    private Double clp;
    @JsonProperty("cnh")
    private Double cnh;
    @JsonProperty("cny")
    private Double cny;
    @JsonProperty("comp")
    private Double comp;
    @JsonProperty("cop")
    private Double cop;
    @JsonProperty("crc")
    private Double crc;
    @JsonProperty("cro")
    private Double cro;
    @JsonProperty("crv")
    private Double crv;
    @JsonProperty("cspr")
    private Double cspr;
    @JsonProperty("cuc")
    private Integer cuc;
    @JsonProperty("cup")
    private Double cup;
    @JsonProperty("cve")
    private Double cve;
    @JsonProperty("cvx")
    private Double cvx;
    @JsonProperty("cyp")
    private Double cyp;
    @JsonProperty("czk")
    private Double czk;
    @JsonProperty("dai")
    private Double dai;
    @JsonProperty("dash")
    private Double dash;
    @JsonProperty("dcr")
    private Double dcr;
    @JsonProperty("dem")
    private Double dem;
    @JsonProperty("dfi")
    private Double dfi;
    @JsonProperty("djf")
    private Double djf;
    @JsonProperty("dkk")
    private Double dkk;
    @JsonProperty("doge")
    private Double doge;
    @JsonProperty("dop")
    private Double dop;
    @JsonProperty("dot")
    private Double dot;
    @JsonProperty("dydx")
    private Double dydx;
    @JsonProperty("dzd")
    private Double dzd;
    @JsonProperty("eek")
    private Double eek;
    @JsonProperty("egld")
    private Double egld;
    @JsonProperty("egp")
    private Double egp;
    @JsonProperty("enj")
    private Double enj;
    @JsonProperty("eos")
    private Double eos;
    @JsonProperty("ern")
    private Integer ern;
    @JsonProperty("esp")
    private Double esp;
    @JsonProperty("etb")
    private Double etb;
    @JsonProperty("etc")
    private Double etc;
    @JsonProperty("eth")
    private Double eth;
    @JsonProperty("eur")
    private Double eur;
    @JsonProperty("fei")
    private Double fei;
    @JsonProperty("fil")
    private Double fil;
    @JsonProperty("fim")
    private Double fim;
    @JsonProperty("fjd")
    private Double fjd;
    @JsonProperty("fkp")
    private Double fkp;
    @JsonProperty("flow")
    private Double flow;
    @JsonProperty("flr")
    private Double flr;
    @JsonProperty("frax")
    private Double frax;
    @JsonProperty("frf")
    private Double frf;
    @JsonProperty("ftm")
    private Double ftm;
    @JsonProperty("ftt")
    private Double ftt;
    @JsonProperty("fxs")
    private Double fxs;
    @JsonProperty("gala")
    private Double gala;
    @JsonProperty("gbp")
    private Double gbp;
    @JsonProperty("gel")
    private Double gel;
    @JsonProperty("ggp")
    private Double ggp;
    @JsonProperty("ghc")
    private Double ghc;
    @JsonProperty("ghs")
    private Double ghs;
    @JsonProperty("gip")
    private Double gip;
    @JsonProperty("gmd")
    private Double gmd;
    @JsonProperty("gmx")
    private Double gmx;
    @JsonProperty("gnf")
    private Double gnf;
    @JsonProperty("gno")
    private Double gno;
    @JsonProperty("grd")
    private Double grd;
    @JsonProperty("grt")
    private Double grt;
    @JsonProperty("gt")
    private Double gt;
    @JsonProperty("gtq")
    private Double gtq;
    @JsonProperty("gusd")
    private Double gusd;
    @JsonProperty("gyd")
    private Double gyd;
    @JsonProperty("hbar")
    private Double hbar;
    @JsonProperty("hkd")
    private Double hkd;
    @JsonProperty("hnl")
    private Double hnl;
    @JsonProperty("hnt")
    private Double hnt;
    @JsonProperty("hot")
    private Double hot;
    @JsonProperty("hrk")
    private Double hrk;
    @JsonProperty("ht")
    private Double ht;
    @JsonProperty("htg")
    private Double htg;
    @JsonProperty("huf")
    private Double huf;
    @JsonProperty("icp")
    private Double icp;
    @JsonProperty("idr")
    private Double idr;
    @JsonProperty("iep")
    private Double iep;
    @JsonProperty("ils")
    private Double ils;
    @JsonProperty("imp")
    private Double imp;
    @JsonProperty("imx")
    private Double imx;
    @JsonProperty("inj")
    private Double inj;
    @JsonProperty("inr")
    private Double inr;
    @JsonProperty("iqd")
    private Double iqd;
    @JsonProperty("irr")
    private Double irr;
    @JsonProperty("isk")
    private Double isk;
    @JsonProperty("itl")
    private Double itl;
    @JsonProperty("jep")
    private Double jep;
    @JsonProperty("jmd")
    private Double jmd;
    @JsonProperty("jod")
    private Double jod;
    @JsonProperty("jpy")
    private Double jpy;
    @JsonProperty("kas")
    private Double kas;
    @JsonProperty("kava")
    private Double kava;
    @JsonProperty("kcs")
    private Double kcs;
    @JsonProperty("kda")
    private Double kda;
    @JsonProperty("kes")
    private Double kes;
    @JsonProperty("kgs")
    private Double kgs;
    @JsonProperty("khr")
    private Double khr;
    @JsonProperty("klay")
    private Double klay;
    @JsonProperty("kmf")
    private Double kmf;
    @JsonProperty("knc")
    private Double knc;
    @JsonProperty("kpw")
    private Double kpw;
    @JsonProperty("krw")
    private Double krw;
    @JsonProperty("ksm")
    private Double ksm;
    @JsonProperty("kwd")
    private Double kwd;
    @JsonProperty("kyd")
    private Double kyd;
    @JsonProperty("kzt")
    private Double kzt;
    @JsonProperty("lak")
    private Double lak;
    @JsonProperty("lbp")
    private Double lbp;
    @JsonProperty("ldo")
    private Double ldo;
    @JsonProperty("leo")
    private Double leo;
    @JsonProperty("link")
    private Double link;
    @JsonProperty("lkr")
    private Double lkr;
    @JsonProperty("lrc")
    private Double lrc;
    @JsonProperty("lrd")
    private Double lrd;
    @JsonProperty("lsl")
    private Double lsl;
    @JsonProperty("ltc")
    private Double ltc;
    @JsonProperty("ltl")
    private Double ltl;
    @JsonProperty("luf")
    private Double luf;
    @JsonProperty("luna")
    private Double luna;
    @JsonProperty("lunc")
    private Double lunc;
    @JsonProperty("lvl")
    private Double lvl;
    @JsonProperty("lyd")
    private Double lyd;
    @JsonProperty("mad")
    private Double mad;
    @JsonProperty("mana")
    private Double mana;
    @JsonProperty("matic")
    private Double matic;
    @JsonProperty("mbx")
    private Double mbx;
    @JsonProperty("mdl")
    private Double mdl;
    @JsonProperty("mga")
    private Double mga;
    @JsonProperty("mgf")
    private Double mgf;
    @JsonProperty("mina")
    private Double mina;
    @JsonProperty("mkd")
    private Double mkd;
    @JsonProperty("mkr")
    private Double mkr;
    @JsonProperty("mmk")
    private Double mmk;
    @JsonProperty("mnt")
    private Double mnt;
    @JsonProperty("mop")
    private Double mop;
    @JsonProperty("mro")
    private Double mro;
    @JsonProperty("mru")
    private Double mru;
    @JsonProperty("mtl")
    private Double mtl;
    @JsonProperty("mur")
    private Double mur;
    @JsonProperty("mvr")
    private Double mvr;
    @JsonProperty("mwk")
    private Double mwk;
    @JsonProperty("mxn")
    private Double mxn;
    @JsonProperty("mxv")
    private Double mxv;
    @JsonProperty("myr")
    private Double myr;
    @JsonProperty("mzm")
    private Double mzm;
    @JsonProperty("mzn")
    private Double mzn;
    @JsonProperty("nad")
    private Double nad;
    @JsonProperty("near")
    private Double near;
    @JsonProperty("neo")
    private Double neo;
    @JsonProperty("nexo")
    private Double nexo;
    @JsonProperty("nft")
    private Double nft;
    @JsonProperty("ngn")
    private Double ngn;
    @JsonProperty("nio")
    private Double nio;
    @JsonProperty("nlg")
    private Double nlg;
    @JsonProperty("nok")
    private Double nok;
    @JsonProperty("npr")
    private Double npr;
    @JsonProperty("nzd")
    private Double nzd;
    @JsonProperty("okb")
    private Double okb;
    @JsonProperty("omr")
    private Double omr;
    @JsonProperty("one")
    private Double one;
    @JsonProperty("op")
    private Double op;
    @JsonProperty("ordi")
    private Double ordi;
    @JsonProperty("pab")
    private Integer pab;
    @JsonProperty("paxg")
    private Double paxg;
    @JsonProperty("pen")
    private Double pen;
    @JsonProperty("pepe")
    private Double pepe;
    @JsonProperty("pgk")
    private Double pgk;
    @JsonProperty("php")
    private Double php;
    @JsonProperty("pkr")
    private Double pkr;
    @JsonProperty("pln")
    private Double pln;
    @JsonProperty("pte")
    private Double pte;
    @JsonProperty("pyg")
    private Double pyg;
    @JsonProperty("qar")
    private Double qar;
    @JsonProperty("qnt")
    private Double qnt;
    @JsonProperty("qtum")
    private Double qtum;
    @JsonProperty("rol")
    private Double rol;
    @JsonProperty("ron")
    private Double ron;
    @JsonProperty("rpl")
    private Double rpl;
    @JsonProperty("rsd")
    private Double rsd;
    @JsonProperty("rub")
    private Double rub;
    @JsonProperty("rune")
    private Double rune;
    @JsonProperty("rvn")
    private Double rvn;
    @JsonProperty("rwf")
    private Double rwf;
    @JsonProperty("sand")
    private Double sand;
    @JsonProperty("sar")
    private Double sar;
    @JsonProperty("sbd")
    private Double sbd;
    @JsonProperty("scr")
    private Double scr;
    @JsonProperty("sdd")
    private Double sdd;
    @JsonProperty("sdg")
    private Double sdg;
    @JsonProperty("sek")
    private Double sek;
    @JsonProperty("sgd")
    private Double sgd;
    @JsonProperty("shib")
    private Double shib;
    @JsonProperty("shp")
    private Double shp;
    @JsonProperty("sit")
    private Double sit;
    @JsonProperty("skk")
    private Double skk;
    @JsonProperty("sle")
    private Double sle;
    @JsonProperty("sll")
    private Double sll;
    @JsonProperty("snx")
    private Double snx;
    @JsonProperty("sol")
    private Double sol;
    @JsonProperty("sos")
    private Double sos;
    @JsonProperty("spl")
    private Double spl;
    @JsonProperty("srd")
    private Double srd;
    @JsonProperty("srg")
    private Double srg;
    @JsonProperty("std")
    private Double std;
    @JsonProperty("stn")
    private Double stn;
    @JsonProperty("stx")
    private Double stx;
    @JsonProperty("sui")
    private Double sui;
    @JsonProperty("svc")
    private Double svc;
    @JsonProperty("syp")
    private Double syp;
    @JsonProperty("szl")
    private Double szl;
    @JsonProperty("thb")
    private Double thb;
    @JsonProperty("theta")
    private Double theta;
    @JsonProperty("tjs")
    private Double tjs;
    @JsonProperty("tmm")
    private Double tmm;
    @JsonProperty("tmt")
    private Double tmt;
    @JsonProperty("tnd")
    private Double tnd;
    @JsonProperty("ton")
    private Double ton;
    @JsonProperty("top")
    private Double top;
    @JsonProperty("trl")
    private Double trl;
    @JsonProperty("trx")
    private Double trx;
    @JsonProperty("try")
    private Double _try;
    @JsonProperty("ttd")
    private Double ttd;
    @JsonProperty("tusd")
    private Double tusd;
    @JsonProperty("tvd")
    private Double tvd;
    @JsonProperty("twd")
    private Double twd;
    @JsonProperty("twt")
    private Double twt;
    @JsonProperty("tzs")
    private Double tzs;
    @JsonProperty("uah")
    private Double uah;
    @JsonProperty("ugx")
    private Double ugx;
    @JsonProperty("uni")
    private Double uni;
    @JsonProperty("usd")
    private Integer usd;
    @JsonProperty("usdc")
    private Double usdc;
    @JsonProperty("usdd")
    private Double usdd;
    @JsonProperty("usdp")
    private Double usdp;
    @JsonProperty("usdt")
    private Double usdt;
    @JsonProperty("uyu")
    private Double uyu;
    @JsonProperty("uzs")
    private Double uzs;
    @JsonProperty("val")
    private Double val;
    @JsonProperty("veb")
    private Double veb;
    @JsonProperty("ved")
    private Double ved;
    @JsonProperty("vef")
    private Double vef;
    @JsonProperty("ves")
    private Double ves;
    @JsonProperty("vet")
    private Double vet;
    @JsonProperty("vnd")
    private Double vnd;
    @JsonProperty("vuv")
    private Double vuv;
    @JsonProperty("waves")
    private Double waves;
    @JsonProperty("wemix")
    private Double wemix;
    @JsonProperty("woo")
    private Double woo;
    @JsonProperty("wst")
    private Double wst;
    @JsonProperty("xaf")
    private Double xaf;
    @JsonProperty("xag")
    private Double xag;
    @JsonProperty("xau")
    private Double xau;
    @JsonProperty("xaut")
    private Double xaut;
    @JsonProperty("xbt")
    private Double xbt;
    @JsonProperty("xcd")
    private Double xcd;
    @JsonProperty("xch")
    private Double xch;
    @JsonProperty("xdc")
    private Double xdc;
    @JsonProperty("xdr")
    private Double xdr;
    @JsonProperty("xec")
    private Double xec;
    @JsonProperty("xem")
    private Double xem;
    @JsonProperty("xlm")
    private Double xlm;
    @JsonProperty("xmr")
    private Double xmr;
    @JsonProperty("xof")
    private Double xof;
    @JsonProperty("xpd")
    private Double xpd;
    @JsonProperty("xpf")
    private Double xpf;
    @JsonProperty("xpt")
    private Double xpt;
    @JsonProperty("xrp")
    private Double xrp;
    @JsonProperty("xtz")
    private Double xtz;
    @JsonProperty("yer")
    private Double yer;
    @JsonProperty("zar")
    private Double zar;
    @JsonProperty("zec")
    private Double zec;
    @JsonProperty("zil")
    private Double zil;
    @JsonProperty("zmk")
    private Double zmk;
    @JsonProperty("zmw")
    private Double zmw;
    @JsonProperty("zwd")
    private Double zwd;
    @JsonProperty("zwg")
    private Double zwg;
    @JsonProperty("zwl")
    private Double zwl;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("1000sats")
    public Double get1000sats() {
        return _1000sats;
    }

    @JsonProperty("1000sats")
    public void set1000sats(Double _1000sats) {
        this._1000sats = _1000sats;
    }

    @JsonProperty("1inch")
    public Double get1inch() {
        return _1inch;
    }

    @JsonProperty("1inch")
    public void set1inch(Double _1inch) {
        this._1inch = _1inch;
    }

    @JsonProperty("aave")
    public Double getAave() {
        return aave;
    }

    @JsonProperty("aave")
    public void setAave(Double aave) {
        this.aave = aave;
    }

    @JsonProperty("ada")
    public Double getAda() {
        return ada;
    }

    @JsonProperty("ada")
    public void setAda(Double ada) {
        this.ada = ada;
    }

    @JsonProperty("aed")
    public Double getAed() {
        return aed;
    }

    @JsonProperty("aed")
    public void setAed(Double aed) {
        this.aed = aed;
    }

    @JsonProperty("afn")
    public Double getAfn() {
        return afn;
    }

    @JsonProperty("afn")
    public void setAfn(Double afn) {
        this.afn = afn;
    }

    @JsonProperty("agix")
    public Double getAgix() {
        return agix;
    }

    @JsonProperty("agix")
    public void setAgix(Double agix) {
        this.agix = agix;
    }

    @JsonProperty("akt")
    public Double getAkt() {
        return akt;
    }

    @JsonProperty("akt")
    public void setAkt(Double akt) {
        this.akt = akt;
    }

    @JsonProperty("algo")
    public Double getAlgo() {
        return algo;
    }

    @JsonProperty("algo")
    public void setAlgo(Double algo) {
        this.algo = algo;
    }

    @JsonProperty("all")
    public Double getAll() {
        return all;
    }

    @JsonProperty("all")
    public void setAll(Double all) {
        this.all = all;
    }

    @JsonProperty("amd")
    public Double getAmd() {
        return amd;
    }

    @JsonProperty("amd")
    public void setAmd(Double amd) {
        this.amd = amd;
    }

    @JsonProperty("amp")
    public Double getAmp() {
        return amp;
    }

    @JsonProperty("amp")
    public void setAmp(Double amp) {
        this.amp = amp;
    }

    @JsonProperty("ang")
    public Double getAng() {
        return ang;
    }

    @JsonProperty("ang")
    public void setAng(Double ang) {
        this.ang = ang;
    }

    @JsonProperty("aoa")
    public Double getAoa() {
        return aoa;
    }

    @JsonProperty("aoa")
    public void setAoa(Double aoa) {
        this.aoa = aoa;
    }

    @JsonProperty("ape")
    public Double getApe() {
        return ape;
    }

    @JsonProperty("ape")
    public void setApe(Double ape) {
        this.ape = ape;
    }

    @JsonProperty("apt")
    public Double getApt() {
        return apt;
    }

    @JsonProperty("apt")
    public void setApt(Double apt) {
        this.apt = apt;
    }

    @JsonProperty("ar")
    public Double getAr() {
        return ar;
    }

    @JsonProperty("ar")
    public void setAr(Double ar) {
        this.ar = ar;
    }

    @JsonProperty("arb")
    public Double getArb() {
        return arb;
    }

    @JsonProperty("arb")
    public void setArb(Double arb) {
        this.arb = arb;
    }

    @JsonProperty("ars")
    public Double getArs() {
        return ars;
    }

    @JsonProperty("ars")
    public void setArs(Double ars) {
        this.ars = ars;
    }

    @JsonProperty("atom")
    public Double getAtom() {
        return atom;
    }

    @JsonProperty("atom")
    public void setAtom(Double atom) {
        this.atom = atom;
    }

    @JsonProperty("ats")
    public Double getAts() {
        return ats;
    }

    @JsonProperty("ats")
    public void setAts(Double ats) {
        this.ats = ats;
    }

    @JsonProperty("aud")
    public Double getAud() {
        return aud;
    }

    @JsonProperty("aud")
    public void setAud(Double aud) {
        this.aud = aud;
    }

    @JsonProperty("avax")
    public Double getAvax() {
        return avax;
    }

    @JsonProperty("avax")
    public void setAvax(Double avax) {
        this.avax = avax;
    }

    @JsonProperty("awg")
    public Double getAwg() {
        return awg;
    }

    @JsonProperty("awg")
    public void setAwg(Double awg) {
        this.awg = awg;
    }

    @JsonProperty("axs")
    public Double getAxs() {
        return axs;
    }

    @JsonProperty("axs")
    public void setAxs(Double axs) {
        this.axs = axs;
    }

    @JsonProperty("azm")
    public Double getAzm() {
        return azm;
    }

    @JsonProperty("azm")
    public void setAzm(Double azm) {
        this.azm = azm;
    }

    @JsonProperty("azn")
    public Double getAzn() {
        return azn;
    }

    @JsonProperty("azn")
    public void setAzn(Double azn) {
        this.azn = azn;
    }

    @JsonProperty("bake")
    public Double getBake() {
        return bake;
    }

    @JsonProperty("bake")
    public void setBake(Double bake) {
        this.bake = bake;
    }

    @JsonProperty("bam")
    public Double getBam() {
        return bam;
    }

    @JsonProperty("bam")
    public void setBam(Double bam) {
        this.bam = bam;
    }

    @JsonProperty("bat")
    public Double getBat() {
        return bat;
    }

    @JsonProperty("bat")
    public void setBat(Double bat) {
        this.bat = bat;
    }

    @JsonProperty("bbd")
    public Integer getBbd() {
        return bbd;
    }

    @JsonProperty("bbd")
    public void setBbd(Integer bbd) {
        this.bbd = bbd;
    }

    @JsonProperty("bch")
    public Double getBch() {
        return bch;
    }

    @JsonProperty("bch")
    public void setBch(Double bch) {
        this.bch = bch;
    }

    @JsonProperty("bdt")
    public Double getBdt() {
        return bdt;
    }

    @JsonProperty("bdt")
    public void setBdt(Double bdt) {
        this.bdt = bdt;
    }

    @JsonProperty("bef")
    public Double getBef() {
        return bef;
    }

    @JsonProperty("bef")
    public void setBef(Double bef) {
        this.bef = bef;
    }

    @JsonProperty("bgn")
    public Double getBgn() {
        return bgn;
    }

    @JsonProperty("bgn")
    public void setBgn(Double bgn) {
        this.bgn = bgn;
    }

    @JsonProperty("bhd")
    public Double getBhd() {
        return bhd;
    }

    @JsonProperty("bhd")
    public void setBhd(Double bhd) {
        this.bhd = bhd;
    }

    @JsonProperty("bif")
    public Double getBif() {
        return bif;
    }

    @JsonProperty("bif")
    public void setBif(Double bif) {
        this.bif = bif;
    }

    @JsonProperty("bmd")
    public Integer getBmd() {
        return bmd;
    }

    @JsonProperty("bmd")
    public void setBmd(Integer bmd) {
        this.bmd = bmd;
    }

    @JsonProperty("bnb")
    public Double getBnb() {
        return bnb;
    }

    @JsonProperty("bnb")
    public void setBnb(Double bnb) {
        this.bnb = bnb;
    }

    @JsonProperty("bnd")
    public Double getBnd() {
        return bnd;
    }

    @JsonProperty("bnd")
    public void setBnd(Double bnd) {
        this.bnd = bnd;
    }

    @JsonProperty("bob")
    public Double getBob() {
        return bob;
    }

    @JsonProperty("bob")
    public void setBob(Double bob) {
        this.bob = bob;
    }

    @JsonProperty("brl")
    public Double getBrl() {
        return brl;
    }

    @JsonProperty("brl")
    public void setBrl(Double brl) {
        this.brl = brl;
    }

    @JsonProperty("bsd")
    public Integer getBsd() {
        return bsd;
    }

    @JsonProperty("bsd")
    public void setBsd(Integer bsd) {
        this.bsd = bsd;
    }

    @JsonProperty("bsv")
    public Double getBsv() {
        return bsv;
    }

    @JsonProperty("bsv")
    public void setBsv(Double bsv) {
        this.bsv = bsv;
    }

    @JsonProperty("bsw")
    public Double getBsw() {
        return bsw;
    }

    @JsonProperty("bsw")
    public void setBsw(Double bsw) {
        this.bsw = bsw;
    }

    @JsonProperty("btc")
    public Double getBtc() {
        return btc;
    }

    @JsonProperty("btc")
    public void setBtc(Double btc) {
        this.btc = btc;
    }

    @JsonProperty("btcb")
    public Double getBtcb() {
        return btcb;
    }

    @JsonProperty("btcb")
    public void setBtcb(Double btcb) {
        this.btcb = btcb;
    }

    @JsonProperty("btg")
    public Double getBtg() {
        return btg;
    }

    @JsonProperty("btg")
    public void setBtg(Double btg) {
        this.btg = btg;
    }

    @JsonProperty("btn")
    public Double getBtn() {
        return btn;
    }

    @JsonProperty("btn")
    public void setBtn(Double btn) {
        this.btn = btn;
    }

    @JsonProperty("btt")
    public Double getBtt() {
        return btt;
    }

    @JsonProperty("btt")
    public void setBtt(Double btt) {
        this.btt = btt;
    }

    @JsonProperty("busd")
    public Double getBusd() {
        return busd;
    }

    @JsonProperty("busd")
    public void setBusd(Double busd) {
        this.busd = busd;
    }

    @JsonProperty("bwp")
    public Double getBwp() {
        return bwp;
    }

    @JsonProperty("bwp")
    public void setBwp(Double bwp) {
        this.bwp = bwp;
    }

    @JsonProperty("byn")
    public Double getByn() {
        return byn;
    }

    @JsonProperty("byn")
    public void setByn(Double byn) {
        this.byn = byn;
    }

    @JsonProperty("byr")
    public Double getByr() {
        return byr;
    }

    @JsonProperty("byr")
    public void setByr(Double byr) {
        this.byr = byr;
    }

    @JsonProperty("bzd")
    public Double getBzd() {
        return bzd;
    }

    @JsonProperty("bzd")
    public void setBzd(Double bzd) {
        this.bzd = bzd;
    }

    @JsonProperty("cad")
    public Double getCad() {
        return cad;
    }

    @JsonProperty("cad")
    public void setCad(Double cad) {
        this.cad = cad;
    }

    @JsonProperty("cake")
    public Double getCake() {
        return cake;
    }

    @JsonProperty("cake")
    public void setCake(Double cake) {
        this.cake = cake;
    }

    @JsonProperty("cdf")
    public Double getCdf() {
        return cdf;
    }

    @JsonProperty("cdf")
    public void setCdf(Double cdf) {
        this.cdf = cdf;
    }

    @JsonProperty("celo")
    public Double getCelo() {
        return celo;
    }

    @JsonProperty("celo")
    public void setCelo(Double celo) {
        this.celo = celo;
    }

    @JsonProperty("cfx")
    public Double getCfx() {
        return cfx;
    }

    @JsonProperty("cfx")
    public void setCfx(Double cfx) {
        this.cfx = cfx;
    }

    @JsonProperty("chf")
    public Double getChf() {
        return chf;
    }

    @JsonProperty("chf")
    public void setChf(Double chf) {
        this.chf = chf;
    }

    @JsonProperty("chz")
    public Double getChz() {
        return chz;
    }

    @JsonProperty("chz")
    public void setChz(Double chz) {
        this.chz = chz;
    }

    @JsonProperty("clp")
    public Double getClp() {
        return clp;
    }

    @JsonProperty("clp")
    public void setClp(Double clp) {
        this.clp = clp;
    }

    @JsonProperty("cnh")
    public Double getCnh() {
        return cnh;
    }

    @JsonProperty("cnh")
    public void setCnh(Double cnh) {
        this.cnh = cnh;
    }

    @JsonProperty("cny")
    public Double getCny() {
        return cny;
    }

    @JsonProperty("cny")
    public void setCny(Double cny) {
        this.cny = cny;
    }

    @JsonProperty("comp")
    public Double getComp() {
        return comp;
    }

    @JsonProperty("comp")
    public void setComp(Double comp) {
        this.comp = comp;
    }

    @JsonProperty("cop")
    public Double getCop() {
        return cop;
    }

    @JsonProperty("cop")
    public void setCop(Double cop) {
        this.cop = cop;
    }

    @JsonProperty("crc")
    public Double getCrc() {
        return crc;
    }

    @JsonProperty("crc")
    public void setCrc(Double crc) {
        this.crc = crc;
    }

    @JsonProperty("cro")
    public Double getCro() {
        return cro;
    }

    @JsonProperty("cro")
    public void setCro(Double cro) {
        this.cro = cro;
    }

    @JsonProperty("crv")
    public Double getCrv() {
        return crv;
    }

    @JsonProperty("crv")
    public void setCrv(Double crv) {
        this.crv = crv;
    }

    @JsonProperty("cspr")
    public Double getCspr() {
        return cspr;
    }

    @JsonProperty("cspr")
    public void setCspr(Double cspr) {
        this.cspr = cspr;
    }

    @JsonProperty("cuc")
    public Integer getCuc() {
        return cuc;
    }

    @JsonProperty("cuc")
    public void setCuc(Integer cuc) {
        this.cuc = cuc;
    }

    @JsonProperty("cup")
    public Double getCup() {
        return cup;
    }

    @JsonProperty("cup")
    public void setCup(Double cup) {
        this.cup = cup;
    }

    @JsonProperty("cve")
    public Double getCve() {
        return cve;
    }

    @JsonProperty("cve")
    public void setCve(Double cve) {
        this.cve = cve;
    }

    @JsonProperty("cvx")
    public Double getCvx() {
        return cvx;
    }

    @JsonProperty("cvx")
    public void setCvx(Double cvx) {
        this.cvx = cvx;
    }

    @JsonProperty("cyp")
    public Double getCyp() {
        return cyp;
    }

    @JsonProperty("cyp")
    public void setCyp(Double cyp) {
        this.cyp = cyp;
    }

    @JsonProperty("czk")
    public Double getCzk() {
        return czk;
    }

    @JsonProperty("czk")
    public void setCzk(Double czk) {
        this.czk = czk;
    }

    @JsonProperty("dai")
    public Double getDai() {
        return dai;
    }

    @JsonProperty("dai")
    public void setDai(Double dai) {
        this.dai = dai;
    }

    @JsonProperty("dash")
    public Double getDash() {
        return dash;
    }

    @JsonProperty("dash")
    public void setDash(Double dash) {
        this.dash = dash;
    }

    @JsonProperty("dcr")
    public Double getDcr() {
        return dcr;
    }

    @JsonProperty("dcr")
    public void setDcr(Double dcr) {
        this.dcr = dcr;
    }

    @JsonProperty("dem")
    public Double getDem() {
        return dem;
    }

    @JsonProperty("dem")
    public void setDem(Double dem) {
        this.dem = dem;
    }

    @JsonProperty("dfi")
    public Double getDfi() {
        return dfi;
    }

    @JsonProperty("dfi")
    public void setDfi(Double dfi) {
        this.dfi = dfi;
    }

    @JsonProperty("djf")
    public Double getDjf() {
        return djf;
    }

    @JsonProperty("djf")
    public void setDjf(Double djf) {
        this.djf = djf;
    }

    @JsonProperty("dkk")
    public Double getDkk() {
        return dkk;
    }

    @JsonProperty("dkk")
    public void setDkk(Double dkk) {
        this.dkk = dkk;
    }

    @JsonProperty("doge")
    public Double getDoge() {
        return doge;
    }

    @JsonProperty("doge")
    public void setDoge(Double doge) {
        this.doge = doge;
    }

    @JsonProperty("dop")
    public Double getDop() {
        return dop;
    }

    @JsonProperty("dop")
    public void setDop(Double dop) {
        this.dop = dop;
    }

    @JsonProperty("dot")
    public Double getDot() {
        return dot;
    }

    @JsonProperty("dot")
    public void setDot(Double dot) {
        this.dot = dot;
    }

    @JsonProperty("dydx")
    public Double getDydx() {
        return dydx;
    }

    @JsonProperty("dydx")
    public void setDydx(Double dydx) {
        this.dydx = dydx;
    }

    @JsonProperty("dzd")
    public Double getDzd() {
        return dzd;
    }

    @JsonProperty("dzd")
    public void setDzd(Double dzd) {
        this.dzd = dzd;
    }

    @JsonProperty("eek")
    public Double getEek() {
        return eek;
    }

    @JsonProperty("eek")
    public void setEek(Double eek) {
        this.eek = eek;
    }

    @JsonProperty("egld")
    public Double getEgld() {
        return egld;
    }

    @JsonProperty("egld")
    public void setEgld(Double egld) {
        this.egld = egld;
    }

    @JsonProperty("egp")
    public Double getEgp() {
        return egp;
    }

    @JsonProperty("egp")
    public void setEgp(Double egp) {
        this.egp = egp;
    }

    @JsonProperty("enj")
    public Double getEnj() {
        return enj;
    }

    @JsonProperty("enj")
    public void setEnj(Double enj) {
        this.enj = enj;
    }

    @JsonProperty("eos")
    public Double getEos() {
        return eos;
    }

    @JsonProperty("eos")
    public void setEos(Double eos) {
        this.eos = eos;
    }

    @JsonProperty("ern")
    public Integer getErn() {
        return ern;
    }

    @JsonProperty("ern")
    public void setErn(Integer ern) {
        this.ern = ern;
    }

    @JsonProperty("esp")
    public Double getEsp() {
        return esp;
    }

    @JsonProperty("esp")
    public void setEsp(Double esp) {
        this.esp = esp;
    }

    @JsonProperty("etb")
    public Double getEtb() {
        return etb;
    }

    @JsonProperty("etb")
    public void setEtb(Double etb) {
        this.etb = etb;
    }

    @JsonProperty("etc")
    public Double getEtc() {
        return etc;
    }

    @JsonProperty("etc")
    public void setEtc(Double etc) {
        this.etc = etc;
    }

    @JsonProperty("eth")
    public Double getEth() {
        return eth;
    }

    @JsonProperty("eth")
    public void setEth(Double eth) {
        this.eth = eth;
    }

    @JsonProperty("eur")
    public Double getEur() {
        return eur;
    }

    @JsonProperty("eur")
    public void setEur(Double eur) {
        this.eur = eur;
    }

    @JsonProperty("fei")
    public Double getFei() {
        return fei;
    }

    @JsonProperty("fei")
    public void setFei(Double fei) {
        this.fei = fei;
    }

    @JsonProperty("fil")
    public Double getFil() {
        return fil;
    }

    @JsonProperty("fil")
    public void setFil(Double fil) {
        this.fil = fil;
    }

    @JsonProperty("fim")
    public Double getFim() {
        return fim;
    }

    @JsonProperty("fim")
    public void setFim(Double fim) {
        this.fim = fim;
    }

    @JsonProperty("fjd")
    public Double getFjd() {
        return fjd;
    }

    @JsonProperty("fjd")
    public void setFjd(Double fjd) {
        this.fjd = fjd;
    }

    @JsonProperty("fkp")
    public Double getFkp() {
        return fkp;
    }

    @JsonProperty("fkp")
    public void setFkp(Double fkp) {
        this.fkp = fkp;
    }

    @JsonProperty("flow")
    public Double getFlow() {
        return flow;
    }

    @JsonProperty("flow")
    public void setFlow(Double flow) {
        this.flow = flow;
    }

    @JsonProperty("flr")
    public Double getFlr() {
        return flr;
    }

    @JsonProperty("flr")
    public void setFlr(Double flr) {
        this.flr = flr;
    }

    @JsonProperty("frax")
    public Double getFrax() {
        return frax;
    }

    @JsonProperty("frax")
    public void setFrax(Double frax) {
        this.frax = frax;
    }

    @JsonProperty("frf")
    public Double getFrf() {
        return frf;
    }

    @JsonProperty("frf")
    public void setFrf(Double frf) {
        this.frf = frf;
    }

    @JsonProperty("ftm")
    public Double getFtm() {
        return ftm;
    }

    @JsonProperty("ftm")
    public void setFtm(Double ftm) {
        this.ftm = ftm;
    }

    @JsonProperty("ftt")
    public Double getFtt() {
        return ftt;
    }

    @JsonProperty("ftt")
    public void setFtt(Double ftt) {
        this.ftt = ftt;
    }

    @JsonProperty("fxs")
    public Double getFxs() {
        return fxs;
    }

    @JsonProperty("fxs")
    public void setFxs(Double fxs) {
        this.fxs = fxs;
    }

    @JsonProperty("gala")
    public Double getGala() {
        return gala;
    }

    @JsonProperty("gala")
    public void setGala(Double gala) {
        this.gala = gala;
    }

    @JsonProperty("gbp")
    public Double getGbp() {
        return gbp;
    }

    @JsonProperty("gbp")
    public void setGbp(Double gbp) {
        this.gbp = gbp;
    }

    @JsonProperty("gel")
    public Double getGel() {
        return gel;
    }

    @JsonProperty("gel")
    public void setGel(Double gel) {
        this.gel = gel;
    }

    @JsonProperty("ggp")
    public Double getGgp() {
        return ggp;
    }

    @JsonProperty("ggp")
    public void setGgp(Double ggp) {
        this.ggp = ggp;
    }

    @JsonProperty("ghc")
    public Double getGhc() {
        return ghc;
    }

    @JsonProperty("ghc")
    public void setGhc(Double ghc) {
        this.ghc = ghc;
    }

    @JsonProperty("ghs")
    public Double getGhs() {
        return ghs;
    }

    @JsonProperty("ghs")
    public void setGhs(Double ghs) {
        this.ghs = ghs;
    }

    @JsonProperty("gip")
    public Double getGip() {
        return gip;
    }

    @JsonProperty("gip")
    public void setGip(Double gip) {
        this.gip = gip;
    }

    @JsonProperty("gmd")
    public Double getGmd() {
        return gmd;
    }

    @JsonProperty("gmd")
    public void setGmd(Double gmd) {
        this.gmd = gmd;
    }

    @JsonProperty("gmx")
    public Double getGmx() {
        return gmx;
    }

    @JsonProperty("gmx")
    public void setGmx(Double gmx) {
        this.gmx = gmx;
    }

    @JsonProperty("gnf")
    public Double getGnf() {
        return gnf;
    }

    @JsonProperty("gnf")
    public void setGnf(Double gnf) {
        this.gnf = gnf;
    }

    @JsonProperty("gno")
    public Double getGno() {
        return gno;
    }

    @JsonProperty("gno")
    public void setGno(Double gno) {
        this.gno = gno;
    }

    @JsonProperty("grd")
    public Double getGrd() {
        return grd;
    }

    @JsonProperty("grd")
    public void setGrd(Double grd) {
        this.grd = grd;
    }

    @JsonProperty("grt")
    public Double getGrt() {
        return grt;
    }

    @JsonProperty("grt")
    public void setGrt(Double grt) {
        this.grt = grt;
    }

    @JsonProperty("gt")
    public Double getGt() {
        return gt;
    }

    @JsonProperty("gt")
    public void setGt(Double gt) {
        this.gt = gt;
    }

    @JsonProperty("gtq")
    public Double getGtq() {
        return gtq;
    }

    @JsonProperty("gtq")
    public void setGtq(Double gtq) {
        this.gtq = gtq;
    }

    @JsonProperty("gusd")
    public Double getGusd() {
        return gusd;
    }

    @JsonProperty("gusd")
    public void setGusd(Double gusd) {
        this.gusd = gusd;
    }

    @JsonProperty("gyd")
    public Double getGyd() {
        return gyd;
    }

    @JsonProperty("gyd")
    public void setGyd(Double gyd) {
        this.gyd = gyd;
    }

    @JsonProperty("hbar")
    public Double getHbar() {
        return hbar;
    }

    @JsonProperty("hbar")
    public void setHbar(Double hbar) {
        this.hbar = hbar;
    }

    @JsonProperty("hkd")
    public Double getHkd() {
        return hkd;
    }

    @JsonProperty("hkd")
    public void setHkd(Double hkd) {
        this.hkd = hkd;
    }

    @JsonProperty("hnl")
    public Double getHnl() {
        return hnl;
    }

    @JsonProperty("hnl")
    public void setHnl(Double hnl) {
        this.hnl = hnl;
    }

    @JsonProperty("hnt")
    public Double getHnt() {
        return hnt;
    }

    @JsonProperty("hnt")
    public void setHnt(Double hnt) {
        this.hnt = hnt;
    }

    @JsonProperty("hot")
    public Double getHot() {
        return hot;
    }

    @JsonProperty("hot")
    public void setHot(Double hot) {
        this.hot = hot;
    }

    @JsonProperty("hrk")
    public Double getHrk() {
        return hrk;
    }

    @JsonProperty("hrk")
    public void setHrk(Double hrk) {
        this.hrk = hrk;
    }

    @JsonProperty("ht")
    public Double getHt() {
        return ht;
    }

    @JsonProperty("ht")
    public void setHt(Double ht) {
        this.ht = ht;
    }

    @JsonProperty("htg")
    public Double getHtg() {
        return htg;
    }

    @JsonProperty("htg")
    public void setHtg(Double htg) {
        this.htg = htg;
    }

    @JsonProperty("huf")
    public Double getHuf() {
        return huf;
    }

    @JsonProperty("huf")
    public void setHuf(Double huf) {
        this.huf = huf;
    }

    @JsonProperty("icp")
    public Double getIcp() {
        return icp;
    }

    @JsonProperty("icp")
    public void setIcp(Double icp) {
        this.icp = icp;
    }

    @JsonProperty("idr")
    public Double getIdr() {
        return idr;
    }

    @JsonProperty("idr")
    public void setIdr(Double idr) {
        this.idr = idr;
    }

    @JsonProperty("iep")
    public Double getIep() {
        return iep;
    }

    @JsonProperty("iep")
    public void setIep(Double iep) {
        this.iep = iep;
    }

    @JsonProperty("ils")
    public Double getIls() {
        return ils;
    }

    @JsonProperty("ils")
    public void setIls(Double ils) {
        this.ils = ils;
    }

    @JsonProperty("imp")
    public Double getImp() {
        return imp;
    }

    @JsonProperty("imp")
    public void setImp(Double imp) {
        this.imp = imp;
    }

    @JsonProperty("imx")
    public Double getImx() {
        return imx;
    }

    @JsonProperty("imx")
    public void setImx(Double imx) {
        this.imx = imx;
    }

    @JsonProperty("inj")
    public Double getInj() {
        return inj;
    }

    @JsonProperty("inj")
    public void setInj(Double inj) {
        this.inj = inj;
    }

    @JsonProperty("inr")
    public Double getInr() {
        return inr;
    }

    @JsonProperty("inr")
    public void setInr(Double inr) {
        this.inr = inr;
    }

    @JsonProperty("iqd")
    public Double getIqd() {
        return iqd;
    }

    @JsonProperty("iqd")
    public void setIqd(Double iqd) {
        this.iqd = iqd;
    }

    @JsonProperty("irr")
    public Double getIrr() {
        return irr;
    }

    @JsonProperty("irr")
    public void setIrr(Double irr) {
        this.irr = irr;
    }

    @JsonProperty("isk")
    public Double getIsk() {
        return isk;
    }

    @JsonProperty("isk")
    public void setIsk(Double isk) {
        this.isk = isk;
    }

    @JsonProperty("itl")
    public Double getItl() {
        return itl;
    }

    @JsonProperty("itl")
    public void setItl(Double itl) {
        this.itl = itl;
    }

    @JsonProperty("jep")
    public Double getJep() {
        return jep;
    }

    @JsonProperty("jep")
    public void setJep(Double jep) {
        this.jep = jep;
    }

    @JsonProperty("jmd")
    public Double getJmd() {
        return jmd;
    }

    @JsonProperty("jmd")
    public void setJmd(Double jmd) {
        this.jmd = jmd;
    }

    @JsonProperty("jod")
    public Double getJod() {
        return jod;
    }

    @JsonProperty("jod")
    public void setJod(Double jod) {
        this.jod = jod;
    }

    @JsonProperty("jpy")
    public Double getJpy() {
        return jpy;
    }

    @JsonProperty("jpy")
    public void setJpy(Double jpy) {
        this.jpy = jpy;
    }

    @JsonProperty("kas")
    public Double getKas() {
        return kas;
    }

    @JsonProperty("kas")
    public void setKas(Double kas) {
        this.kas = kas;
    }

    @JsonProperty("kava")
    public Double getKava() {
        return kava;
    }

    @JsonProperty("kava")
    public void setKava(Double kava) {
        this.kava = kava;
    }

    @JsonProperty("kcs")
    public Double getKcs() {
        return kcs;
    }

    @JsonProperty("kcs")
    public void setKcs(Double kcs) {
        this.kcs = kcs;
    }

    @JsonProperty("kda")
    public Double getKda() {
        return kda;
    }

    @JsonProperty("kda")
    public void setKda(Double kda) {
        this.kda = kda;
    }

    @JsonProperty("kes")
    public Double getKes() {
        return kes;
    }

    @JsonProperty("kes")
    public void setKes(Double kes) {
        this.kes = kes;
    }

    @JsonProperty("kgs")
    public Double getKgs() {
        return kgs;
    }

    @JsonProperty("kgs")
    public void setKgs(Double kgs) {
        this.kgs = kgs;
    }

    @JsonProperty("khr")
    public Double getKhr() {
        return khr;
    }

    @JsonProperty("khr")
    public void setKhr(Double khr) {
        this.khr = khr;
    }

    @JsonProperty("klay")
    public Double getKlay() {
        return klay;
    }

    @JsonProperty("klay")
    public void setKlay(Double klay) {
        this.klay = klay;
    }

    @JsonProperty("kmf")
    public Double getKmf() {
        return kmf;
    }

    @JsonProperty("kmf")
    public void setKmf(Double kmf) {
        this.kmf = kmf;
    }

    @JsonProperty("knc")
    public Double getKnc() {
        return knc;
    }

    @JsonProperty("knc")
    public void setKnc(Double knc) {
        this.knc = knc;
    }

    @JsonProperty("kpw")
    public Double getKpw() {
        return kpw;
    }

    @JsonProperty("kpw")
    public void setKpw(Double kpw) {
        this.kpw = kpw;
    }

    @JsonProperty("krw")
    public Double getKrw() {
        return krw;
    }

    @JsonProperty("krw")
    public void setKrw(Double krw) {
        this.krw = krw;
    }

    @JsonProperty("ksm")
    public Double getKsm() {
        return ksm;
    }

    @JsonProperty("ksm")
    public void setKsm(Double ksm) {
        this.ksm = ksm;
    }

    @JsonProperty("kwd")
    public Double getKwd() {
        return kwd;
    }

    @JsonProperty("kwd")
    public void setKwd(Double kwd) {
        this.kwd = kwd;
    }

    @JsonProperty("kyd")
    public Double getKyd() {
        return kyd;
    }

    @JsonProperty("kyd")
    public void setKyd(Double kyd) {
        this.kyd = kyd;
    }

    @JsonProperty("kzt")
    public Double getKzt() {
        return kzt;
    }

    @JsonProperty("kzt")
    public void setKzt(Double kzt) {
        this.kzt = kzt;
    }

    @JsonProperty("lak")
    public Double getLak() {
        return lak;
    }

    @JsonProperty("lak")
    public void setLak(Double lak) {
        this.lak = lak;
    }

    @JsonProperty("lbp")
    public Double getLbp() {
        return lbp;
    }

    @JsonProperty("lbp")
    public void setLbp(Double lbp) {
        this.lbp = lbp;
    }

    @JsonProperty("ldo")
    public Double getLdo() {
        return ldo;
    }

    @JsonProperty("ldo")
    public void setLdo(Double ldo) {
        this.ldo = ldo;
    }

    @JsonProperty("leo")
    public Double getLeo() {
        return leo;
    }

    @JsonProperty("leo")
    public void setLeo(Double leo) {
        this.leo = leo;
    }

    @JsonProperty("link")
    public Double getLink() {
        return link;
    }

    @JsonProperty("link")
    public void setLink(Double link) {
        this.link = link;
    }

    @JsonProperty("lkr")
    public Double getLkr() {
        return lkr;
    }

    @JsonProperty("lkr")
    public void setLkr(Double lkr) {
        this.lkr = lkr;
    }

    @JsonProperty("lrc")
    public Double getLrc() {
        return lrc;
    }

    @JsonProperty("lrc")
    public void setLrc(Double lrc) {
        this.lrc = lrc;
    }

    @JsonProperty("lrd")
    public Double getLrd() {
        return lrd;
    }

    @JsonProperty("lrd")
    public void setLrd(Double lrd) {
        this.lrd = lrd;
    }

    @JsonProperty("lsl")
    public Double getLsl() {
        return lsl;
    }

    @JsonProperty("lsl")
    public void setLsl(Double lsl) {
        this.lsl = lsl;
    }

    @JsonProperty("ltc")
    public Double getLtc() {
        return ltc;
    }

    @JsonProperty("ltc")
    public void setLtc(Double ltc) {
        this.ltc = ltc;
    }

    @JsonProperty("ltl")
    public Double getLtl() {
        return ltl;
    }

    @JsonProperty("ltl")
    public void setLtl(Double ltl) {
        this.ltl = ltl;
    }

    @JsonProperty("luf")
    public Double getLuf() {
        return luf;
    }

    @JsonProperty("luf")
    public void setLuf(Double luf) {
        this.luf = luf;
    }

    @JsonProperty("luna")
    public Double getLuna() {
        return luna;
    }

    @JsonProperty("luna")
    public void setLuna(Double luna) {
        this.luna = luna;
    }

    @JsonProperty("lunc")
    public Double getLunc() {
        return lunc;
    }

    @JsonProperty("lunc")
    public void setLunc(Double lunc) {
        this.lunc = lunc;
    }

    @JsonProperty("lvl")
    public Double getLvl() {
        return lvl;
    }

    @JsonProperty("lvl")
    public void setLvl(Double lvl) {
        this.lvl = lvl;
    }

    @JsonProperty("lyd")
    public Double getLyd() {
        return lyd;
    }

    @JsonProperty("lyd")
    public void setLyd(Double lyd) {
        this.lyd = lyd;
    }

    @JsonProperty("mad")
    public Double getMad() {
        return mad;
    }

    @JsonProperty("mad")
    public void setMad(Double mad) {
        this.mad = mad;
    }

    @JsonProperty("mana")
    public Double getMana() {
        return mana;
    }

    @JsonProperty("mana")
    public void setMana(Double mana) {
        this.mana = mana;
    }

    @JsonProperty("matic")
    public Double getMatic() {
        return matic;
    }

    @JsonProperty("matic")
    public void setMatic(Double matic) {
        this.matic = matic;
    }

    @JsonProperty("mbx")
    public Double getMbx() {
        return mbx;
    }

    @JsonProperty("mbx")
    public void setMbx(Double mbx) {
        this.mbx = mbx;
    }

    @JsonProperty("mdl")
    public Double getMdl() {
        return mdl;
    }

    @JsonProperty("mdl")
    public void setMdl(Double mdl) {
        this.mdl = mdl;
    }

    @JsonProperty("mga")
    public Double getMga() {
        return mga;
    }

    @JsonProperty("mga")
    public void setMga(Double mga) {
        this.mga = mga;
    }

    @JsonProperty("mgf")
    public Double getMgf() {
        return mgf;
    }

    @JsonProperty("mgf")
    public void setMgf(Double mgf) {
        this.mgf = mgf;
    }

    @JsonProperty("mina")
    public Double getMina() {
        return mina;
    }

    @JsonProperty("mina")
    public void setMina(Double mina) {
        this.mina = mina;
    }

    @JsonProperty("mkd")
    public Double getMkd() {
        return mkd;
    }

    @JsonProperty("mkd")
    public void setMkd(Double mkd) {
        this.mkd = mkd;
    }

    @JsonProperty("mkr")
    public Double getMkr() {
        return mkr;
    }

    @JsonProperty("mkr")
    public void setMkr(Double mkr) {
        this.mkr = mkr;
    }

    @JsonProperty("mmk")
    public Double getMmk() {
        return mmk;
    }

    @JsonProperty("mmk")
    public void setMmk(Double mmk) {
        this.mmk = mmk;
    }

    @JsonProperty("mnt")
    public Double getMnt() {
        return mnt;
    }

    @JsonProperty("mnt")
    public void setMnt(Double mnt) {
        this.mnt = mnt;
    }

    @JsonProperty("mop")
    public Double getMop() {
        return mop;
    }

    @JsonProperty("mop")
    public void setMop(Double mop) {
        this.mop = mop;
    }

    @JsonProperty("mro")
    public Double getMro() {
        return mro;
    }

    @JsonProperty("mro")
    public void setMro(Double mro) {
        this.mro = mro;
    }

    @JsonProperty("mru")
    public Double getMru() {
        return mru;
    }

    @JsonProperty("mru")
    public void setMru(Double mru) {
        this.mru = mru;
    }

    @JsonProperty("mtl")
    public Double getMtl() {
        return mtl;
    }

    @JsonProperty("mtl")
    public void setMtl(Double mtl) {
        this.mtl = mtl;
    }

    @JsonProperty("mur")
    public Double getMur() {
        return mur;
    }

    @JsonProperty("mur")
    public void setMur(Double mur) {
        this.mur = mur;
    }

    @JsonProperty("mvr")
    public Double getMvr() {
        return mvr;
    }

    @JsonProperty("mvr")
    public void setMvr(Double mvr) {
        this.mvr = mvr;
    }

    @JsonProperty("mwk")
    public Double getMwk() {
        return mwk;
    }

    @JsonProperty("mwk")
    public void setMwk(Double mwk) {
        this.mwk = mwk;
    }

    @JsonProperty("mxn")
    public Double getMxn() {
        return mxn;
    }

    @JsonProperty("mxn")
    public void setMxn(Double mxn) {
        this.mxn = mxn;
    }

    @JsonProperty("mxv")
    public Double getMxv() {
        return mxv;
    }

    @JsonProperty("mxv")
    public void setMxv(Double mxv) {
        this.mxv = mxv;
    }

    @JsonProperty("myr")
    public Double getMyr() {
        return myr;
    }

    @JsonProperty("myr")
    public void setMyr(Double myr) {
        this.myr = myr;
    }

    @JsonProperty("mzm")
    public Double getMzm() {
        return mzm;
    }

    @JsonProperty("mzm")
    public void setMzm(Double mzm) {
        this.mzm = mzm;
    }

    @JsonProperty("mzn")
    public Double getMzn() {
        return mzn;
    }

    @JsonProperty("mzn")
    public void setMzn(Double mzn) {
        this.mzn = mzn;
    }

    @JsonProperty("nad")
    public Double getNad() {
        return nad;
    }

    @JsonProperty("nad")
    public void setNad(Double nad) {
        this.nad = nad;
    }

    @JsonProperty("near")
    public Double getNear() {
        return near;
    }

    @JsonProperty("near")
    public void setNear(Double near) {
        this.near = near;
    }

    @JsonProperty("neo")
    public Double getNeo() {
        return neo;
    }

    @JsonProperty("neo")
    public void setNeo(Double neo) {
        this.neo = neo;
    }

    @JsonProperty("nexo")
    public Double getNexo() {
        return nexo;
    }

    @JsonProperty("nexo")
    public void setNexo(Double nexo) {
        this.nexo = nexo;
    }

    @JsonProperty("nft")
    public Double getNft() {
        return nft;
    }

    @JsonProperty("nft")
    public void setNft(Double nft) {
        this.nft = nft;
    }

    @JsonProperty("ngn")
    public Double getNgn() {
        return ngn;
    }

    @JsonProperty("ngn")
    public void setNgn(Double ngn) {
        this.ngn = ngn;
    }

    @JsonProperty("nio")
    public Double getNio() {
        return nio;
    }

    @JsonProperty("nio")
    public void setNio(Double nio) {
        this.nio = nio;
    }

    @JsonProperty("nlg")
    public Double getNlg() {
        return nlg;
    }

    @JsonProperty("nlg")
    public void setNlg(Double nlg) {
        this.nlg = nlg;
    }

    @JsonProperty("nok")
    public Double getNok() {
        return nok;
    }

    @JsonProperty("nok")
    public void setNok(Double nok) {
        this.nok = nok;
    }

    @JsonProperty("npr")
    public Double getNpr() {
        return npr;
    }

    @JsonProperty("npr")
    public void setNpr(Double npr) {
        this.npr = npr;
    }

    @JsonProperty("nzd")
    public Double getNzd() {
        return nzd;
    }

    @JsonProperty("nzd")
    public void setNzd(Double nzd) {
        this.nzd = nzd;
    }

    @JsonProperty("okb")
    public Double getOkb() {
        return okb;
    }

    @JsonProperty("okb")
    public void setOkb(Double okb) {
        this.okb = okb;
    }

    @JsonProperty("omr")
    public Double getOmr() {
        return omr;
    }

    @JsonProperty("omr")
    public void setOmr(Double omr) {
        this.omr = omr;
    }

    @JsonProperty("one")
    public Double getOne() {
        return one;
    }

    @JsonProperty("one")
    public void setOne(Double one) {
        this.one = one;
    }

    @JsonProperty("op")
    public Double getOp() {
        return op;
    }

    @JsonProperty("op")
    public void setOp(Double op) {
        this.op = op;
    }

    @JsonProperty("ordi")
    public Double getOrdi() {
        return ordi;
    }

    @JsonProperty("ordi")
    public void setOrdi(Double ordi) {
        this.ordi = ordi;
    }

    @JsonProperty("pab")
    public Integer getPab() {
        return pab;
    }

    @JsonProperty("pab")
    public void setPab(Integer pab) {
        this.pab = pab;
    }

    @JsonProperty("paxg")
    public Double getPaxg() {
        return paxg;
    }

    @JsonProperty("paxg")
    public void setPaxg(Double paxg) {
        this.paxg = paxg;
    }

    @JsonProperty("pen")
    public Double getPen() {
        return pen;
    }

    @JsonProperty("pen")
    public void setPen(Double pen) {
        this.pen = pen;
    }

    @JsonProperty("pepe")
    public Double getPepe() {
        return pepe;
    }

    @JsonProperty("pepe")
    public void setPepe(Double pepe) {
        this.pepe = pepe;
    }

    @JsonProperty("pgk")
    public Double getPgk() {
        return pgk;
    }

    @JsonProperty("pgk")
    public void setPgk(Double pgk) {
        this.pgk = pgk;
    }

    @JsonProperty("php")
    public Double getPhp() {
        return php;
    }

    @JsonProperty("php")
    public void setPhp(Double php) {
        this.php = php;
    }

    @JsonProperty("pkr")
    public Double getPkr() {
        return pkr;
    }

    @JsonProperty("pkr")
    public void setPkr(Double pkr) {
        this.pkr = pkr;
    }

    @JsonProperty("pln")
    public Double getPln() {
        return pln;
    }

    @JsonProperty("pln")
    public void setPln(Double pln) {
        this.pln = pln;
    }

    @JsonProperty("pte")
    public Double getPte() {
        return pte;
    }

    @JsonProperty("pte")
    public void setPte(Double pte) {
        this.pte = pte;
    }

    @JsonProperty("pyg")
    public Double getPyg() {
        return pyg;
    }

    @JsonProperty("pyg")
    public void setPyg(Double pyg) {
        this.pyg = pyg;
    }

    @JsonProperty("qar")
    public Double getQar() {
        return qar;
    }

    @JsonProperty("qar")
    public void setQar(Double qar) {
        this.qar = qar;
    }

    @JsonProperty("qnt")
    public Double getQnt() {
        return qnt;
    }

    @JsonProperty("qnt")
    public void setQnt(Double qnt) {
        this.qnt = qnt;
    }

    @JsonProperty("qtum")
    public Double getQtum() {
        return qtum;
    }

    @JsonProperty("qtum")
    public void setQtum(Double qtum) {
        this.qtum = qtum;
    }

    @JsonProperty("rol")
    public Double getRol() {
        return rol;
    }

    @JsonProperty("rol")
    public void setRol(Double rol) {
        this.rol = rol;
    }

    @JsonProperty("ron")
    public Double getRon() {
        return ron;
    }

    @JsonProperty("ron")
    public void setRon(Double ron) {
        this.ron = ron;
    }

    @JsonProperty("rpl")
    public Double getRpl() {
        return rpl;
    }

    @JsonProperty("rpl")
    public void setRpl(Double rpl) {
        this.rpl = rpl;
    }

    @JsonProperty("rsd")
    public Double getRsd() {
        return rsd;
    }

    @JsonProperty("rsd")
    public void setRsd(Double rsd) {
        this.rsd = rsd;
    }

    @JsonProperty("rub")
    public Double getRub() {
        return rub;
    }

    @JsonProperty("rub")
    public void setRub(Double rub) {
        this.rub = rub;
    }

    @JsonProperty("rune")
    public Double getRune() {
        return rune;
    }

    @JsonProperty("rune")
    public void setRune(Double rune) {
        this.rune = rune;
    }

    @JsonProperty("rvn")
    public Double getRvn() {
        return rvn;
    }

    @JsonProperty("rvn")
    public void setRvn(Double rvn) {
        this.rvn = rvn;
    }

    @JsonProperty("rwf")
    public Double getRwf() {
        return rwf;
    }

    @JsonProperty("rwf")
    public void setRwf(Double rwf) {
        this.rwf = rwf;
    }

    @JsonProperty("sand")
    public Double getSand() {
        return sand;
    }

    @JsonProperty("sand")
    public void setSand(Double sand) {
        this.sand = sand;
    }

    @JsonProperty("sar")
    public Double getSar() {
        return sar;
    }

    @JsonProperty("sar")
    public void setSar(Double sar) {
        this.sar = sar;
    }

    @JsonProperty("sbd")
    public Double getSbd() {
        return sbd;
    }

    @JsonProperty("sbd")
    public void setSbd(Double sbd) {
        this.sbd = sbd;
    }

    @JsonProperty("scr")
    public Double getScr() {
        return scr;
    }

    @JsonProperty("scr")
    public void setScr(Double scr) {
        this.scr = scr;
    }

    @JsonProperty("sdd")
    public Double getSdd() {
        return sdd;
    }

    @JsonProperty("sdd")
    public void setSdd(Double sdd) {
        this.sdd = sdd;
    }

    @JsonProperty("sdg")
    public Double getSdg() {
        return sdg;
    }

    @JsonProperty("sdg")
    public void setSdg(Double sdg) {
        this.sdg = sdg;
    }

    @JsonProperty("sek")
    public Double getSek() {
        return sek;
    }

    @JsonProperty("sek")
    public void setSek(Double sek) {
        this.sek = sek;
    }

    @JsonProperty("sgd")
    public Double getSgd() {
        return sgd;
    }

    @JsonProperty("sgd")
    public void setSgd(Double sgd) {
        this.sgd = sgd;
    }

    @JsonProperty("shib")
    public Double getShib() {
        return shib;
    }

    @JsonProperty("shib")
    public void setShib(Double shib) {
        this.shib = shib;
    }

    @JsonProperty("shp")
    public Double getShp() {
        return shp;
    }

    @JsonProperty("shp")
    public void setShp(Double shp) {
        this.shp = shp;
    }

    @JsonProperty("sit")
    public Double getSit() {
        return sit;
    }

    @JsonProperty("sit")
    public void setSit(Double sit) {
        this.sit = sit;
    }

    @JsonProperty("skk")
    public Double getSkk() {
        return skk;
    }

    @JsonProperty("skk")
    public void setSkk(Double skk) {
        this.skk = skk;
    }

    @JsonProperty("sle")
    public Double getSle() {
        return sle;
    }

    @JsonProperty("sle")
    public void setSle(Double sle) {
        this.sle = sle;
    }

    @JsonProperty("sll")
    public Double getSll() {
        return sll;
    }

    @JsonProperty("sll")
    public void setSll(Double sll) {
        this.sll = sll;
    }

    @JsonProperty("snx")
    public Double getSnx() {
        return snx;
    }

    @JsonProperty("snx")
    public void setSnx(Double snx) {
        this.snx = snx;
    }

    @JsonProperty("sol")
    public Double getSol() {
        return sol;
    }

    @JsonProperty("sol")
    public void setSol(Double sol) {
        this.sol = sol;
    }

    @JsonProperty("sos")
    public Double getSos() {
        return sos;
    }

    @JsonProperty("sos")
    public void setSos(Double sos) {
        this.sos = sos;
    }

    @JsonProperty("spl")
    public Double getSpl() {
        return spl;
    }

    @JsonProperty("spl")
    public void setSpl(Double spl) {
        this.spl = spl;
    }

    @JsonProperty("srd")
    public Double getSrd() {
        return srd;
    }

    @JsonProperty("srd")
    public void setSrd(Double srd) {
        this.srd = srd;
    }

    @JsonProperty("srg")
    public Double getSrg() {
        return srg;
    }

    @JsonProperty("srg")
    public void setSrg(Double srg) {
        this.srg = srg;
    }

    @JsonProperty("std")
    public Double getStd() {
        return std;
    }

    @JsonProperty("std")
    public void setStd(Double std) {
        this.std = std;
    }

    @JsonProperty("stn")
    public Double getStn() {
        return stn;
    }

    @JsonProperty("stn")
    public void setStn(Double stn) {
        this.stn = stn;
    }

    @JsonProperty("stx")
    public Double getStx() {
        return stx;
    }

    @JsonProperty("stx")
    public void setStx(Double stx) {
        this.stx = stx;
    }

    @JsonProperty("sui")
    public Double getSui() {
        return sui;
    }

    @JsonProperty("sui")
    public void setSui(Double sui) {
        this.sui = sui;
    }

    @JsonProperty("svc")
    public Double getSvc() {
        return svc;
    }

    @JsonProperty("svc")
    public void setSvc(Double svc) {
        this.svc = svc;
    }

    @JsonProperty("syp")
    public Double getSyp() {
        return syp;
    }

    @JsonProperty("syp")
    public void setSyp(Double syp) {
        this.syp = syp;
    }

    @JsonProperty("szl")
    public Double getSzl() {
        return szl;
    }

    @JsonProperty("szl")
    public void setSzl(Double szl) {
        this.szl = szl;
    }

    @JsonProperty("thb")
    public Double getThb() {
        return thb;
    }

    @JsonProperty("thb")
    public void setThb(Double thb) {
        this.thb = thb;
    }

    @JsonProperty("theta")
    public Double getTheta() {
        return theta;
    }

    @JsonProperty("theta")
    public void setTheta(Double theta) {
        this.theta = theta;
    }

    @JsonProperty("tjs")
    public Double getTjs() {
        return tjs;
    }

    @JsonProperty("tjs")
    public void setTjs(Double tjs) {
        this.tjs = tjs;
    }

    @JsonProperty("tmm")
    public Double getTmm() {
        return tmm;
    }

    @JsonProperty("tmm")
    public void setTmm(Double tmm) {
        this.tmm = tmm;
    }

    @JsonProperty("tmt")
    public Double getTmt() {
        return tmt;
    }

    @JsonProperty("tmt")
    public void setTmt(Double tmt) {
        this.tmt = tmt;
    }

    @JsonProperty("tnd")
    public Double getTnd() {
        return tnd;
    }

    @JsonProperty("tnd")
    public void setTnd(Double tnd) {
        this.tnd = tnd;
    }

    @JsonProperty("ton")
    public Double getTon() {
        return ton;
    }

    @JsonProperty("ton")
    public void setTon(Double ton) {
        this.ton = ton;
    }

    @JsonProperty("top")
    public Double getTop() {
        return top;
    }

    @JsonProperty("top")
    public void setTop(Double top) {
        this.top = top;
    }

    @JsonProperty("trl")
    public Double getTrl() {
        return trl;
    }

    @JsonProperty("trl")
    public void setTrl(Double trl) {
        this.trl = trl;
    }

    @JsonProperty("trx")
    public Double getTrx() {
        return trx;
    }

    @JsonProperty("trx")
    public void setTrx(Double trx) {
        this.trx = trx;
    }

    @JsonProperty("try")
    public Double getTry() {
        return _try;
    }

    @JsonProperty("try")
    public void setTry(Double _try) {
        this._try = _try;
    }

    @JsonProperty("ttd")
    public Double getTtd() {
        return ttd;
    }

    @JsonProperty("ttd")
    public void setTtd(Double ttd) {
        this.ttd = ttd;
    }

    @JsonProperty("tusd")
    public Double getTusd() {
        return tusd;
    }

    @JsonProperty("tusd")
    public void setTusd(Double tusd) {
        this.tusd = tusd;
    }

    @JsonProperty("tvd")
    public Double getTvd() {
        return tvd;
    }

    @JsonProperty("tvd")
    public void setTvd(Double tvd) {
        this.tvd = tvd;
    }

    @JsonProperty("twd")
    public Double getTwd() {
        return twd;
    }

    @JsonProperty("twd")
    public void setTwd(Double twd) {
        this.twd = twd;
    }

    @JsonProperty("twt")
    public Double getTwt() {
        return twt;
    }

    @JsonProperty("twt")
    public void setTwt(Double twt) {
        this.twt = twt;
    }

    @JsonProperty("tzs")
    public Double getTzs() {
        return tzs;
    }

    @JsonProperty("tzs")
    public void setTzs(Double tzs) {
        this.tzs = tzs;
    }

    @JsonProperty("uah")
    public Double getUah() {
        return uah;
    }

    @JsonProperty("uah")
    public void setUah(Double uah) {
        this.uah = uah;
    }

    @JsonProperty("ugx")
    public Double getUgx() {
        return ugx;
    }

    @JsonProperty("ugx")
    public void setUgx(Double ugx) {
        this.ugx = ugx;
    }

    @JsonProperty("uni")
    public Double getUni() {
        return uni;
    }

    @JsonProperty("uni")
    public void setUni(Double uni) {
        this.uni = uni;
    }

    @JsonProperty("usd")
    public Integer getUsd() {
        return usd;
    }

    @JsonProperty("usd")
    public void setUsd(Integer usd) {
        this.usd = usd;
    }

    @JsonProperty("usdc")
    public Double getUsdc() {
        return usdc;
    }

    @JsonProperty("usdc")
    public void setUsdc(Double usdc) {
        this.usdc = usdc;
    }

    @JsonProperty("usdd")
    public Double getUsdd() {
        return usdd;
    }

    @JsonProperty("usdd")
    public void setUsdd(Double usdd) {
        this.usdd = usdd;
    }

    @JsonProperty("usdp")
    public Double getUsdp() {
        return usdp;
    }

    @JsonProperty("usdp")
    public void setUsdp(Double usdp) {
        this.usdp = usdp;
    }

    @JsonProperty("usdt")
    public Double getUsdt() {
        return usdt;
    }

    @JsonProperty("usdt")
    public void setUsdt(Double usdt) {
        this.usdt = usdt;
    }

    @JsonProperty("uyu")
    public Double getUyu() {
        return uyu;
    }

    @JsonProperty("uyu")
    public void setUyu(Double uyu) {
        this.uyu = uyu;
    }

    @JsonProperty("uzs")
    public Double getUzs() {
        return uzs;
    }

    @JsonProperty("uzs")
    public void setUzs(Double uzs) {
        this.uzs = uzs;
    }

    @JsonProperty("val")
    public Double getVal() {
        return val;
    }

    @JsonProperty("val")
    public void setVal(Double val) {
        this.val = val;
    }

    @JsonProperty("veb")
    public Double getVeb() {
        return veb;
    }

    @JsonProperty("veb")
    public void setVeb(Double veb) {
        this.veb = veb;
    }

    @JsonProperty("ved")
    public Double getVed() {
        return ved;
    }

    @JsonProperty("ved")
    public void setVed(Double ved) {
        this.ved = ved;
    }

    @JsonProperty("vef")
    public Double getVef() {
        return vef;
    }

    @JsonProperty("vef")
    public void setVef(Double vef) {
        this.vef = vef;
    }

    @JsonProperty("ves")
    public Double getVes() {
        return ves;
    }

    @JsonProperty("ves")
    public void setVes(Double ves) {
        this.ves = ves;
    }

    @JsonProperty("vet")
    public Double getVet() {
        return vet;
    }

    @JsonProperty("vet")
    public void setVet(Double vet) {
        this.vet = vet;
    }

    @JsonProperty("vnd")
    public Double getVnd() {
        return vnd;
    }

    @JsonProperty("vnd")
    public void setVnd(Double vnd) {
        this.vnd = vnd;
    }

    @JsonProperty("vuv")
    public Double getVuv() {
        return vuv;
    }

    @JsonProperty("vuv")
    public void setVuv(Double vuv) {
        this.vuv = vuv;
    }

    @JsonProperty("waves")
    public Double getWaves() {
        return waves;
    }

    @JsonProperty("waves")
    public void setWaves(Double waves) {
        this.waves = waves;
    }

    @JsonProperty("wemix")
    public Double getWemix() {
        return wemix;
    }

    @JsonProperty("wemix")
    public void setWemix(Double wemix) {
        this.wemix = wemix;
    }

    @JsonProperty("woo")
    public Double getWoo() {
        return woo;
    }

    @JsonProperty("woo")
    public void setWoo(Double woo) {
        this.woo = woo;
    }

    @JsonProperty("wst")
    public Double getWst() {
        return wst;
    }

    @JsonProperty("wst")
    public void setWst(Double wst) {
        this.wst = wst;
    }

    @JsonProperty("xaf")
    public Double getXaf() {
        return xaf;
    }

    @JsonProperty("xaf")
    public void setXaf(Double xaf) {
        this.xaf = xaf;
    }

    @JsonProperty("xag")
    public Double getXag() {
        return xag;
    }

    @JsonProperty("xag")
    public void setXag(Double xag) {
        this.xag = xag;
    }

    @JsonProperty("xau")
    public Double getXau() {
        return xau;
    }

    @JsonProperty("xau")
    public void setXau(Double xau) {
        this.xau = xau;
    }

    @JsonProperty("xaut")
    public Double getXaut() {
        return xaut;
    }

    @JsonProperty("xaut")
    public void setXaut(Double xaut) {
        this.xaut = xaut;
    }

    @JsonProperty("xbt")
    public Double getXbt() {
        return xbt;
    }

    @JsonProperty("xbt")
    public void setXbt(Double xbt) {
        this.xbt = xbt;
    }

    @JsonProperty("xcd")
    public Double getXcd() {
        return xcd;
    }

    @JsonProperty("xcd")
    public void setXcd(Double xcd) {
        this.xcd = xcd;
    }

    @JsonProperty("xch")
    public Double getXch() {
        return xch;
    }

    @JsonProperty("xch")
    public void setXch(Double xch) {
        this.xch = xch;
    }

    @JsonProperty("xdc")
    public Double getXdc() {
        return xdc;
    }

    @JsonProperty("xdc")
    public void setXdc(Double xdc) {
        this.xdc = xdc;
    }

    @JsonProperty("xdr")
    public Double getXdr() {
        return xdr;
    }

    @JsonProperty("xdr")
    public void setXdr(Double xdr) {
        this.xdr = xdr;
    }

    @JsonProperty("xec")
    public Double getXec() {
        return xec;
    }

    @JsonProperty("xec")
    public void setXec(Double xec) {
        this.xec = xec;
    }

    @JsonProperty("xem")
    public Double getXem() {
        return xem;
    }

    @JsonProperty("xem")
    public void setXem(Double xem) {
        this.xem = xem;
    }

    @JsonProperty("xlm")
    public Double getXlm() {
        return xlm;
    }

    @JsonProperty("xlm")
    public void setXlm(Double xlm) {
        this.xlm = xlm;
    }

    @JsonProperty("xmr")
    public Double getXmr() {
        return xmr;
    }

    @JsonProperty("xmr")
    public void setXmr(Double xmr) {
        this.xmr = xmr;
    }

    @JsonProperty("xof")
    public Double getXof() {
        return xof;
    }

    @JsonProperty("xof")
    public void setXof(Double xof) {
        this.xof = xof;
    }

    @JsonProperty("xpd")
    public Double getXpd() {
        return xpd;
    }

    @JsonProperty("xpd")
    public void setXpd(Double xpd) {
        this.xpd = xpd;
    }

    @JsonProperty("xpf")
    public Double getXpf() {
        return xpf;
    }

    @JsonProperty("xpf")
    public void setXpf(Double xpf) {
        this.xpf = xpf;
    }

    @JsonProperty("xpt")
    public Double getXpt() {
        return xpt;
    }

    @JsonProperty("xpt")
    public void setXpt(Double xpt) {
        this.xpt = xpt;
    }

    @JsonProperty("xrp")
    public Double getXrp() {
        return xrp;
    }

    @JsonProperty("xrp")
    public void setXrp(Double xrp) {
        this.xrp = xrp;
    }

    @JsonProperty("xtz")
    public Double getXtz() {
        return xtz;
    }

    @JsonProperty("xtz")
    public void setXtz(Double xtz) {
        this.xtz = xtz;
    }

    @JsonProperty("yer")
    public Double getYer() {
        return yer;
    }

    @JsonProperty("yer")
    public void setYer(Double yer) {
        this.yer = yer;
    }

    @JsonProperty("zar")
    public Double getZar() {
        return zar;
    }

    @JsonProperty("zar")
    public void setZar(Double zar) {
        this.zar = zar;
    }

    @JsonProperty("zec")
    public Double getZec() {
        return zec;
    }

    @JsonProperty("zec")
    public void setZec(Double zec) {
        this.zec = zec;
    }

    @JsonProperty("zil")
    public Double getZil() {
        return zil;
    }

    @JsonProperty("zil")
    public void setZil(Double zil) {
        this.zil = zil;
    }

    @JsonProperty("zmk")
    public Double getZmk() {
        return zmk;
    }

    @JsonProperty("zmk")
    public void setZmk(Double zmk) {
        this.zmk = zmk;
    }

    @JsonProperty("zmw")
    public Double getZmw() {
        return zmw;
    }

    @JsonProperty("zmw")
    public void setZmw(Double zmw) {
        this.zmw = zmw;
    }

    @JsonProperty("zwd")
    public Double getZwd() {
        return zwd;
    }

    @JsonProperty("zwd")
    public void setZwd(Double zwd) {
        this.zwd = zwd;
    }

    @JsonProperty("zwg")
    public Double getZwg() {
        return zwg;
    }

    @JsonProperty("zwg")
    public void setZwg(Double zwg) {
        this.zwg = zwg;
    }

    @JsonProperty("zwl")
    public Double getZwl() {
        return zwl;
    }

    @JsonProperty("zwl")
    public void setZwl(Double zwl) {
        this.zwl = zwl;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
