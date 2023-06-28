<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:functx="http://www.functx.com">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="content"
                                       page-width="210mm" page-height="297mm"
                                       margin-top="10mm" margin-bottom="10mm"
                                       margin-left="20mm" margin-right="20mm">

                    <fo:region-body />
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="content">
                <fo:flow flow-name="xsl-region-body">
                    <fo:table table-layout="fixed" width="100%">
                        <fo:table-column column-width="25mm" />
                        <fo:table-column column-width="70mm" />
                        <fo:table-column column-width="25mm" />
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell column-number="1">
                                    <fo:block><fo:external-graphic src="src\main\samples\logo.png" content-width="20mm"/></fo:block>
                                </fo:table-cell>
                                <fo:table-cell column-number="2">
                                    <fo:block font-size="16pt" font-family="sans-serif" font-weight="bold">GAI</fo:block>
                                </fo:table-cell>
                                <fo:table-cell column-number="3">
                                    <fo:block
                                            font-family="sans-serif" font-size="24pt"
                                            color="#BBBBBB" text-align="right"
                                            margin-right="18mm">GAIReport</fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>


                    <fo:block space-before="10pt">
                        <fo:table border="1pt solid #888">
                            <fo:table-column column-width="10mm" />
                            <fo:table-column column-width="20mm" />
                            <fo:table-column column-width="30mm" />
                            <fo:table-column column-width="30mm" />
                            <fo:table-column column-width="30mm" />
                            <fo:table-body>
                                <fo:table-row font-size="12pt" background-color="#EEE" border="1pt solid #888">
                                    <fo:table-cell padding="1mm 2mm">
                                        <fo:block>ID</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell padding="1mm 2mm">
                                        <fo:block>Type</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell padding="1mm 2mm">
                                        <fo:block>Penalty</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell padding="1mm 2mm">
                                        <fo:block>Date</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell padding="1mm 2mm">
                                        <fo:block>Driver</fo:block>
                                    </fo:table-cell>

                                </fo:table-row>

                                <xsl:for-each select="root/Order">
                                    <xsl:variable name="background">
                                        <xsl:choose>
                                            <xsl:when test="position() mod 2 = 0">
                                                <xsl:text>#EEE</xsl:text>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:text>#FFF</xsl:text>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </xsl:variable>
                                    <fo:table-row font-size="8pt" background-color="{$background}">
                                        <fo:table-cell padding="1mm 2mm">
                                            <fo:block><xsl:value-of select="ID"/></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell padding="1mm 2mm">
                                            <fo:block><xsl:value-of select="Date"/></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell padding="1mm 2mm">
                                            <fo:block><xsl:value-of select="Status"/></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell padding="1mm 2mm">
                                            <fo:block><xsl:value-of select="Problem"/></fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell padding="1mm 2mm">
                                            <fo:block><xsl:value-of select="Worker"/></fo:block>
                                        </fo:table-cell>
                                    </fo:table-row>
                                </xsl:for-each>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>


                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>