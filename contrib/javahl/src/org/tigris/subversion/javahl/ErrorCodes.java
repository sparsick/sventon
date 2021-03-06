/** ErrorCodes.java - This file is autogenerated by gen-javahl-errors.py
 */

package org.tigris.subversion.javahl;

/**
 * Provide mappings from error codes generated by the C runtime to meaningful
 * Java values.  For a better description of each error, please see
 * svn_error_codes.h in the C source.
 */
public class ErrorCodes
{
    public static final int categorySize = 5000;
    public static final int badContainingPool = 125000;
    public static final int badFilename = 125001;
    public static final int badUrl = 125002;
    public static final int badDate = 125003;
    public static final int badMimeType = 125004;
    public static final int badPropertyValue = 125005;
    public static final int badVersionFileFormat = 125006;
    public static final int badRelativePath = 125007;
    public static final int badUuid = 125008;
    public static final int xmlAttribNotFound = 130000;
    public static final int xmlMissingAncestry = 130001;
    public static final int xmlUnknownEncoding = 130002;
    public static final int xmlMalformed = 130003;
    public static final int xmlUnescapableData = 130004;
    public static final int ioInconsistentEol = 135000;
    public static final int ioUnknownEol = 135001;
    public static final int ioCorruptEol = 135002;
    public static final int ioUniqueNamesExhausted = 135003;
    public static final int ioPipeFrameError = 135004;
    public static final int ioPipeReadError = 135005;
    public static final int ioWriteError = 135006;
    public static final int streamUnexpectedEof = 140000;
    public static final int streamMalformedData = 140001;
    public static final int streamUnrecognizedData = 140002;
    public static final int nodeUnknownKind = 145000;
    public static final int nodeUnexpectedKind = 145001;
    public static final int entryNotFound = 150000;
    public static final int entryExists = 150002;
    public static final int entryMissingRevision = 150003;
    public static final int entryMissingUrl = 150004;
    public static final int entryAttributeInvalid = 150005;
    public static final int wcObstructedUpdate = 155000;
    public static final int wcUnwindMismatch = 155001;
    public static final int wcUnwindEmpty = 155002;
    public static final int wcUnwindNotEmpty = 155003;
    public static final int wcLocked = 155004;
    public static final int wcNotLocked = 155005;
    public static final int wcInvalidLock = 155006;
    public static final int wcNotDirectory = 155007;
    public static final int wcNotFile = 155008;
    public static final int wcBadAdmLog = 155009;
    public static final int wcPathNotFound = 155010;
    public static final int wcNotUpToDate = 155011;
    public static final int wcLeftLocalMod = 155012;
    public static final int wcScheduleConflict = 155013;
    public static final int wcPathFound = 155014;
    public static final int wcFoundConflict = 155015;
    public static final int wcCorrupt = 155016;
    public static final int wcCorruptTextBase = 155017;
    public static final int wcNodeKindChange = 155018;
    public static final int wcInvalidOpOnCwd = 155019;
    public static final int wcBadAdmLogStart = 155020;
    public static final int wcUnsupportedFormat = 155021;
    public static final int wcBadPath = 155022;
    public static final int wcInvalidSchedule = 155023;
    public static final int wcInvalidRelocation = 155024;
    public static final int wcInvalidSwitch = 155025;
    public static final int wcMismatchedChangelist = 155026;
    public static final int wcConflictResolverFailure = 155027;
    public static final int wcCopyfromPathNotFound = 155028;
    public static final int wcChangelistMove = 155029;
    public static final int fsGeneral = 160000;
    public static final int fsCleanup = 160001;
    public static final int fsAlreadyOpen = 160002;
    public static final int fsNotOpen = 160003;
    public static final int fsCorrupt = 160004;
    public static final int fsPathSyntax = 160005;
    public static final int fsNoSuchRevision = 160006;
    public static final int fsNoSuchTransaction = 160007;
    public static final int fsNoSuchEntry = 160008;
    public static final int fsNoSuchRepresentation = 160009;
    public static final int fsNoSuchString = 160010;
    public static final int fsNoSuchCopy = 160011;
    public static final int fsTransactionNotMutable = 160012;
    public static final int fsNotFound = 160013;
    public static final int fsIdNotFound = 160014;
    public static final int fsNotId = 160015;
    public static final int fsNotDirectory = 160016;
    public static final int fsNotFile = 160017;
    public static final int fsNotSinglePathComponent = 160018;
    public static final int fsNotMutable = 160019;
    public static final int fsAlreadyExists = 160020;
    public static final int fsRootDir = 160021;
    public static final int fsNotTxnRoot = 160022;
    public static final int fsNotRevisionRoot = 160023;
    public static final int fsConflict = 160024;
    public static final int fsRepChanged = 160025;
    public static final int fsRepNotMutable = 160026;
    public static final int fsMalformedSkel = 160027;
    public static final int fsTxnOutOfDate = 160028;
    public static final int fsBerkeleyDb = 160029;
    public static final int fsBerkeleyDbDeadlock = 160030;
    public static final int fsTransactionDead = 160031;
    public static final int fsTransactionNotDead = 160032;
    public static final int fsUnknownFsType = 160033;
    public static final int fsNoUser = 160034;
    public static final int fsPathAlreadyLocked = 160035;
    public static final int fsPathNotLocked = 160036;
    public static final int fsBadLockToken = 160037;
    public static final int fsNoLockToken = 160038;
    public static final int fsLockOwnerMismatch = 160039;
    public static final int fsNoSuchLock = 160040;
    public static final int fsLockExpired = 160041;
    public static final int fsOutOfDate = 160042;
    public static final int fsUnsupportedFormat = 160043;
    public static final int fsRepBeingWritten = 160044;
    public static final int fsTxnNameTooLong = 160045;
    public static final int fsNoSuchNodeOrigin = 160046;
    public static final int reposLocked = 165000;
    public static final int reposHookFailure = 165001;
    public static final int reposBadArgs = 165002;
    public static final int reposNoDataForReport = 165003;
    public static final int reposBadRevisionReport = 165004;
    public static final int reposUnsupportedVersion = 165005;
    public static final int reposDisabledFeature = 165006;
    public static final int reposPostCommitHookFailed = 165007;
    public static final int reposPostLockHookFailed = 165008;
    public static final int reposPostUnlockHookFailed = 165009;
    public static final int raIllegalUrl = 170000;
    public static final int raNotAuthorized = 170001;
    public static final int raUnknownAuth = 170002;
    public static final int raNotImplemented = 170003;
    public static final int raOutOfDate = 170004;
    public static final int raNoReposUuid = 170005;
    public static final int raUnsupportedAbiVersion = 170006;
    public static final int raNotLocked = 170007;
    public static final int raUnknownCapability = 170008;
    public static final int raPartialReplayNotSupported = 170009;
    public static final int raDavSockInit = 175000;
    public static final int raDavCreatingRequest = 175001;
    public static final int raDavRequestFailed = 175002;
    public static final int raDavOptionsReqFailed = 175003;
    public static final int raDavPropsNotFound = 175004;
    public static final int raDavAlreadyExists = 175005;
    public static final int raDavInvalidConfigValue = 175006;
    public static final int raDavPathNotFound = 175007;
    public static final int raDavProppatchFailed = 175008;
    public static final int raDavMalformedData = 175009;
    public static final int raDavResponseHeaderBadness = 175010;
    public static final int raDavRelocated = 175011;
    public static final int raLocalReposNotFound = 180000;
    public static final int raLocalReposOpenFailed = 180001;
    public static final int svndiffInvalidHeader = 185000;
    public static final int svndiffCorruptWindow = 185001;
    public static final int svndiffBackwardView = 185002;
    public static final int svndiffInvalidOps = 185003;
    public static final int svndiffUnexpectedEnd = 185004;
    public static final int svndiffInvalidCompressedData = 185005;
    public static final int apmodMissingPathToFs = 190000;
    public static final int apmodMalformedUri = 190001;
    public static final int apmodActivityNotFound = 190002;
    public static final int apmodBadBaseline = 190003;
    public static final int apmodConnectionAborted = 190004;
    public static final int clientVersionedPathRequired = 195000;
    public static final int clientRaAccessRequired = 195001;
    public static final int clientBadRevision = 195002;
    public static final int clientDuplicateCommitUrl = 195003;
    public static final int clientIsBinaryFile = 195004;
    public static final int clientInvalidExternalsDescription = 195005;
    public static final int clientModified = 195006;
    public static final int clientIsDirectory = 195007;
    public static final int clientRevisionRange = 195008;
    public static final int clientInvalidRelocation = 195009;
    public static final int clientRevisionAuthorContainsNewline = 195010;
    public static final int clientPropertyName = 195011;
    public static final int clientUnrelatedResources = 195012;
    public static final int clientMissingLockToken = 195013;
    public static final int clientMultipleSourcesDisallowed = 195014;
    public static final int clientNoVersionedParent = 195015;
    public static final int clientNotReadyToMerge = 195016;
    public static final int base = 200000;
    public static final int pluginLoadFailure = 200001;
    public static final int malformedFile = 200002;
    public static final int incompleteData = 200003;
    public static final int incorrectParams = 200004;
    public static final int unversionedResource = 200005;
    public static final int testFailed = 200006;
    public static final int unsupportedFeature = 200007;
    public static final int badPropKind = 200008;
    public static final int illegalTarget = 200009;
    public static final int deltaMd5ChecksumAbsent = 200010;
    public static final int dirNotEmpty = 200011;
    public static final int externalProgram = 200012;
    public static final int swigPyExceptionSet = 200013;
    public static final int checksumMismatch = 200014;
    public static final int cancelled = 200015;
    public static final int invalidDiffOption = 200016;
    public static final int propertyNotFound = 200017;
    public static final int noAuthFilePath = 200018;
    public static final int versionMismatch = 200019;
    public static final int mergeinfoParseError = 200020;
    public static final int ceaseInvocation = 200021;
    public static final int revnumParseFailure = 200022;
    public static final int iterBreak = 200023;
    public static final int unknownChangelist = 200024;
    public static final int clArgParsingError = 205000;
    public static final int clInsufficientArgs = 205001;
    public static final int clMutuallyExclusiveArgs = 205002;
    public static final int clAdmDirReserved = 205003;
    public static final int clLogMessageIsVersionedFile = 205004;
    public static final int clLogMessageIsPathname = 205005;
    public static final int clCommitInAddedDir = 205006;
    public static final int clNoExternalEditor = 205007;
    public static final int clBadLogMessage = 205008;
    public static final int clUnnecessaryLogMessage = 205009;
    public static final int clNoExternalMergeTool = 205010;
    public static final int last = 205011;
    public static final int raSvnCmdErr = 210000;
    public static final int raSvnUnknownCmd = 210001;
    public static final int raSvnConnectionClosed = 210002;
    public static final int raSvnIoError = 210003;
    public static final int raSvnMalformedData = 210004;
    public static final int raSvnReposNotFound = 210005;
    public static final int raSvnBadVersion = 210006;
    public static final int raSvnNoMechanisms = 210007;
    public static final int authnCredsUnavailable = 215000;
    public static final int authnNoProvider = 215001;
    public static final int authnProvidersExhausted = 215002;
    public static final int authnCredsNotSaved = 215003;
    public static final int authnFailed = 215004;
    public static final int authzRootUnreadable = 220000;
    public static final int authzUnreadable = 220001;
    public static final int authzPartiallyReadable = 220002;
    public static final int authzInvalidConfig = 220003;
    public static final int authzUnwritable = 220004;
    public static final int diffDatasourceModified = 225000;
    public static final int raSerfSspiInitialisationFailed = 230000;
}
