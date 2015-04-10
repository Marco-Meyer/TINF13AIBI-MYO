def constant(f):
    def fget(self):
        return f()
    return property(fget)

class _Const(object):
    @constant
    def DIALOGUE():
        return 0
    @constant
    def TOAST():
        return 1
    @constant
    def KEY():
        return 2
    @constant
    def START_ACTIVITY():
        return 3
    @constant
    def SWIPE_UP():
        return 4
    @constant
    def SWIPE_DOWN():
        return 5
    @constant
    def SWIPE_LEFT():
        return 6
    @constant
    def SWIPE_RIGHT():
        return 7
    @constant
    def TAP_SINGLE():
        return 8
    @constant
    def TAP_DOUBLE():
        return 9
    @constant
    def TAP_HOLD():
        return 10
    @constant
    def KEY_VUZIX_UP():
        return 11
    @constant
    def KEY_VUZIX_DOWN():
        return 12
    @constant
    def KEY_VUZIX_OK():
        return 13

CONST = _Const()

##print(CONST.TAP_HOLD)
##10

##print(CONST.TOAST)
##1