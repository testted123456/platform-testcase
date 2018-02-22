Number.prototype.toFixed = function (j) {
    var h = this + "";
    if (!j) {
        j = 0
    }
    if (h.indexOf(".") == -1) {
        h += "."
    }
    h += new Array(j + 1).join("0");
    if (new RegExp("^(-|\\+)?(\\d+(\\.\\d{0," + (j + 1) + "})?)\\d*$").test(h)) {
        var h = "0" + RegExp.$2, g = RegExp.$1, e = RegExp.$3.length, c = true;
        if (e == j + 2) {
            e = h.match(/\d/g);
            if (parseInt(e[e.length - 1]) > 4) {
                for (var f = e.length - 2; f >= 0; f--) {
                    e[f] = parseInt(e[f]) + 1;
                    if (e[f] == 10) {
                        e[f] = 0;
                        c = f != 1
                    } else {
                        break
                    }
                }
            }
            h = e.join("").replace(new RegExp("(\\d+)(\\d{" + j + "})\\d$"), "$1.$2")
        }
        if (c) {
            h = h.substr(1)
        }
        
        if(g == null){
        	return (h).replace(/\.$/, "")
        }
        return (g + h).replace(/\.$/, "")
    }
    return this + ""
};
var CalcEval = function () {
};
CalcEval.prototype.eval = function (str) {
    var isRetStr = str.match(new RegExp(/^".+"$/g)) != null;
    if (isRetStr == true) {
        str = str.replace(/^"|"$/g, "")
    }
    var bracketsList = this.matchOutBrackets(str);
    for (var i = 0; i < bracketsList.length; i++) {
        var newCalc = bracketsList[i];
        var calcStr = this.eval(newCalc.str.replace(/^\(|\)$/g, ""));
        str = str.replace(newCalc.str, calcStr)
    }
    var errorObj = new Array();
    while (1) {
        var multObj = this.matchExp(str, "/");
        if (multObj == null) {
            break
        }
        var v = this.executeDivi(multObj.firstValue, multObj.secondValue);
        if (v == "NaN") {
            var t = "T" + new Date().getTime();
            errorObj.push({Name: t, Exp: multObj.str});
            str = str.replace(multObj.str, t)
        } else {
            str = str.replace(multObj.str, v)
        }
    }
    while (1) {
        var multObj = this.matchExp(str, "*");
        if (multObj == null) {
            break
        }
        var v = this.executeMult(multObj.firstValue, multObj.secondValue);
        if (v == "NaN") {
            var t = "T" + new Date().getTime();
            errorObj.push({Name: t, Exp: multObj.str});
            str = str.replace(multObj.str, t)
        } else {
            str = str.replace(multObj.str, v)
        }
    }
    
    while (1) {
        var multObj = this.matchExp(str, "-");
        if (multObj == null) {
            break
        }
        var v = this.executeSubt(multObj.firstValue, multObj.secondValue);
        if (v == "NaN") {
            var t = "T" + new Date().getTime();
            errorObj.push({Name: t, Exp: multObj.str});
            str = str.replace(multObj.str, t)
        } else {
            str = str.replace(multObj.str, v)
        }
    }
    
    while (1) {
        var multObj = this.matchExp(str, "+");
        if (multObj == null) {
            break
        }
        var v = this.executeAddi(multObj.firstValue, multObj.secondValue);
        if (v == "NaN" || isNaN(v)) {
            var t = "T" + new Date().getTime();
            errorObj.push({Name: t, Exp: multObj.str});
            str = str.replace(multObj.str, t)
        } else {
            str = str.replace(multObj.str, v)
        }
    }
    
    for (var i = errorObj.length - 1; i >= 0; i--) {
        var ex = errorObj[i];
        str = str.replace(ex.Name, ex.Exp)
    }
    while (1) {
        var multObj = str.match(/\d*={2,3}\d*/g);
        if (multObj == null) {
            break
        }
        var v = eval(multObj[0]);
        str = str.replace(multObj[0], v)
    }
    if (isRetStr == true) {
        try {
            return eval('"' + str + '"')
        } catch (e) {
        }
        try {
            return eval(str)
        } catch (e) {
        }
        return str
    }
    if (str.match(/(^true$)|(^false$)/g)) {
        return str === "true"
    }
    if (!isNaN(Number(str))) {
        return Number(str)
    }
    try {
        return eval(str)
    } catch (e) {
    }
    return str
};
CalcEval.prototype.matchOutBrackets = function (f) {
    var h = new Array();
    if (f == null) {
        return h
    }
    if (typeof f != "string") {
        f = f + ""
    }
    var d = f.split("");
    var a = 0;
    var g = false;
    var b = -1;
    for (var c = 0; c < d.length; c++) {
        if (d[c] == "(") {
            a++;
            g = true;
            if (b == -1) {
                b = c
            }
        }
        if (d[c] == ")") {
            a--
        }
        if (g == true && a == 0) {
            var e = new Object();
            e.str = f.substring(b, c + 1);
            e.firstIndex = b;
            e.lastIndex = c + 1;
            h.push(e);
            b = -1;
            g = false;
            a = 0
        }
    }
    return h
};
CalcEval.prototype.matchExp = function (l, f) {
    var j = null;
    if (l == null) {
        return retList
    }
    if (typeof l != "string") {
        l = l + ""
    }
    var k = l.split("");
    var c = 0;
    var b = 0;
    var g = "";
    var e = "";
    var a = "";
    var d = false;
    for (var h = 0; h <= k.length; h++) {
        if (k[h] == "+" || k[h] == "-" || k[h] == "*" || k[h] == "/" || k[h] == "%" || h == k.length) {
            if (a == "" && k[h] == "-") {
                a += k[h];
                continue
            }
            if (d == true) {
                e = a;
                b = h;
                j = new Object();
                j.firstIndex = c;
                j.secondIndex = b;
                j.str = l.substring(c, b);
                j.firstValue = Number(g);
                j.secondValue = Number(e);
                break
            }
            if (k[h] == null) {
                break
            }
            if (k[h] == f) {
                d = true;
                g = a;
                a = ""
            } else {
                a = "";
                c = -1
            }
        } else {
            a += k[h];
            if (c == -1) {
                c = h
            }
        }
    }
    return j
};
CalcEval.prototype.executeMult = function (d, b) {
    var a = 0, f = d.toString(), c = b.toString();
    try {
        a += f.split(".")[1].length
    } catch (g) {
    }
    try {
        a += c.split(".")[1].length
    } catch (g) {
    }
    return Number(f.replace(".", "")) * Number(c.replace(".", "")) / Math.pow(10, a)
};
CalcEval.prototype.executeDivi = function (arg1, arg2) {
    var t1 = 0, t2 = 0, r1, r2;
    try {
        t1 = arg1.toString().split(".")[1].length
    } catch (e) {
    }
    try {
        t2 = arg2.toString().split(".")[1].length
    } catch (e) {
    }
    with (Math) {
        r1 = Number(arg1.toString().replace(".", ""));
        r2 = Number(arg2.toString().replace(".", ""));
        return this.executeMult(r1 / r2, pow(10, t2 - t1))
    }
};
CalcEval.prototype.executeAddi = function (arg1, arg2) {
    var r1, r2, m;
    try {
        r1 = arg1.toString().split(".")[1].length
    } catch (e) {
        r1 = 0
    }
    try {
        r2 = arg2.toString().split(".")[1].length
    } catch (e) {
        r2 = 0
    }
    m = Math.pow(10, Math.max(r1, r2));
    return (this.eval(arg1 + "*" + m) + this.eval(arg2 + "*" + m)) / m
};
CalcEval.prototype.executeSubt = function (arg1, arg2) {
    var r1, r2, m, n;
    try {
        r1 = arg1.toString().split(".")[1].length
    } catch (e) {
        r1 = 0
    }
    try {
        r2 = arg2.toString().split(".")[1].length
    } catch (e) {
        r2 = 0
    }
    m = Math.pow(10, Math.max(r1, r2));
    n = (r1 >= r2) ? r1 : r2;
    return ((this.eval(arg1 + "*" + m) - this.eval(arg2 + "*" + m)) / m).toFixed(n)
};

function test(str){
	var ce=new CalcEval();
	return ce.eval(str);
}
